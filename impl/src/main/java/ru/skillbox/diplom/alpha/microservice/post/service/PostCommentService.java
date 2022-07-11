package ru.skillbox.diplom.alpha.microservice.post.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ContextedException;
import org.hibernate.PropertyValueException;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.alpha.library.core.dto.AccountByIdDto;
import ru.skillbox.diplom.alpha.library.core.dto.AccountSearchDto;
import ru.skillbox.diplom.alpha.microservice.account.resources.FeignAccountResource;
import ru.skillbox.diplom.alpha.microservice.notification.resource.FeignNotificationResource;
import ru.skillbox.diplom.alpha.microservice.notification.response.NotificationInputDto;
import ru.skillbox.diplom.alpha.microservice.notification.response.NotificationType;
import ru.skillbox.diplom.alpha.microservice.post.dto.*;
import ru.skillbox.diplom.alpha.microservice.post.mapper.PostCommentMapper;
import ru.skillbox.diplom.alpha.microservice.post.mapper.PostMapper;
import ru.skillbox.diplom.alpha.microservice.post.model.*;
import ru.skillbox.diplom.alpha.microservice.post.repository.PostCommentRepository;
import ru.skillbox.diplom.alpha.microservice.post.repository.PostLikeRepository;
import ru.skillbox.diplom.alpha.microservice.post.repository.PostRepository;
import ru.skillbox.diplom.alpha.microservice.post.request.CommentAddRq;
import ru.skillbox.diplom.alpha.microservice.post.response.CommentComplaintRs;
import ru.skillbox.diplom.alpha.microservice.post.response.ComplaintRs;
import ru.skillbox.diplom.alpha.microservice.post.response.PostCommentRs;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.skillbox.diplom.alpha.library.core.repository.NewSpecification.is;
import static ru.skillbox.diplom.alpha.library.core.security.utils.SecurityUtils.getAccountId;


/**
 * PostCommentService
 *
 * @author Ruslan Akbashev, Olga Samoylova
 */
@Setter
@Getter
@Service
@Slf4j
@RequiredArgsConstructor
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;

    private final PostRepository postRepository;

    private final PostLikeRepository postLikeRepository;

    private final FeignAccountResource feignAccountResource;

    private final FeignNotificationResource feignNotificationResource;

    private final PostLikeService likeService;

    PostMapper mapper = Mappers.getMapper(PostMapper.class);

    PostCommentMapper mapperComment = Mappers.getMapper(PostCommentMapper.class);

    public PostCommentRs getCommentByIdPost(int id, int offset, int itemPerPage) {
        log.info("Start - getCommentByIdPost, id =" + id);
        PostCommentRs postCommentRs = new PostCommentRs();
        Pageable pageable = PageRequest.of(offset, itemPerPage);
        List<PostComment> postCommentList = postCommentRepository.findAll(
                getSpecificationComment(new PostCommentSearchDto().setPostId(id)), pageable).toList();
        List<PostCommentDto> postCommentDtoList = mapperComment.
                convertPostCommentListToPostCommentDtoList(postCommentList.stream()
                        .filter(c -> c.getParentId() == null).collect(Collectors.toList()));
        addSubComments(postCommentList, postCommentDtoList);
        Set<Long> accountIdList = new HashSet<>();
        postCommentList.forEach(p -> accountIdList.add(Long.valueOf(p.getAuthorId())));
        List<AccountByIdDto> accountByIdDtoList = feignAccountResource.getAll(
                new AccountSearchDto().setIds(accountIdList.stream().collect(Collectors.toList())));
        for (int j = 0; j < accountByIdDtoList.size(); j++) {
            fillSubComments(postCommentList, postCommentDtoList, accountByIdDtoList.get(j));
        }
        myLikes(postCommentDtoList);
        postCommentRs.setData(postCommentDtoList);
        postCommentRs.setTimestamp(Instant.now().getEpochSecond());
        postCommentRs.setPerPage(itemPerPage);
        postCommentRs.setTotal(0);
        postCommentRs.setOffset(offset);
        log.info("Stop - getCommentByIdPost, id =" + id);
        return postCommentRs;
    }

    private void myLikes(List<PostCommentDto> postCommentDtoList){
        for (PostCommentDto postCommentDto: postCommentDtoList){
            postCommentDto.setMyLike(likeService.getLiked(postCommentDto.getId(), LikeType.COMMENT).getData().get(0).getLiked());
        }
    }


    public PostCommentRs addCommentByIdPost(int id, CommentAddRq request) {
        log.info("Start - addCommentByIdPost, id =" + id);
        PostCommentRs postCommentRs = new PostCommentRs();
        Post post = postRepository.findAll(getSpecification(new PostSearchDto().setId(id))).get(0);
        PostComment postComment = new PostComment();
        postComment.setTime(Instant.now().getEpochSecond());
        postComment.setParentId(request.getParentId() != null ?
                postCommentRepository.findById(request.getParentId()).get().getId() :
                null);
        postComment.setCommentText(request.getCommentText());
        postComment.setPost(post);
        postComment.setAuthorId(getAccountId().intValue());
        postComment.setIsBlocked(false);
        postComment.setIsDelete(false);
        postComment.setLikeAmount(0);
        postCommentRs.addPostCommentDto(
                mapperComment.postCommentToPostCommentDto(postCommentRepository.save(postComment)));
        postCommentRs.setTimestamp(Instant.now().getEpochSecond());
        NotificationInputDto notificationInputDto = new NotificationInputDto();
        if (request.getParentId() == null) {
            log.info("Нотификация комментария для поста");
            notificationInputDto.setUserId(post.getAuthorId().longValue());
            notificationInputDto.setNameNotification(NotificationType.POST_COMMENT);
        } else {
            log.info("Нотификация комментария для комментария");
            PostComment parentComment = postCommentRepository.findById(request.getParentId()).orElseThrow(
                    NullPointerException::new);
            notificationInputDto.setUserId(parentComment.getAuthorId().longValue());
            notificationInputDto.setNameNotification(NotificationType.COMMENT_COMMENT);
        }
        notificationInputDto.setContent(post.getTitle());
        feignNotificationResource.createNotification(notificationInputDto);
        log.info("Stop - addCommentByIdPost, id =" + id);
        return postCommentRs;
    }

    public PostCommentRs putCommentByIdPost(int id, int commentId, CommentAddRq request) throws ContextedException {
        log.info("Start - putCommentByIdPost, id =" + id);
        PostCommentRs postCommentRs = new PostCommentRs();
        PostComment postComment = postCommentRepository.findAll(
                getSpecificationComment(new PostCommentSearchDto().setPostId(id)
                        .setId(commentId))).get(0);
        if (postComment.getAuthorId() != getAccountId().intValue()) {
            log.info("Пользователь не является автором комментария");
            throw new ContextedException("Пользователь не является автором комментария");
        }
        postComment.setParentId(request.getParentId());
        postComment.setCommentText(request.getCommentText());
        postComment.setTime(Instant.now().getEpochSecond());
        postCommentRs.addPostCommentDto(
                mapperComment.postCommentToPostCommentDto(
                        postCommentRepository.save(postComment)));
        postCommentRs.setTimestamp(Instant.now().getEpochSecond());
        log.info("Stop - putCommentByIdPost, id =" + id);
        return postCommentRs;
    }


    public PostCommentRs deleteOrRecoveryCommentByIdPost(int id, int commentId, boolean status) {
        log.info("Start - deleteOrRecoveryCommentByIdPost, id =" + id);
        PostCommentRs postCommentRs = new PostCommentRs();
        PostComment postComment = postCommentRepository.findAll(
                getSpecificationComment(new PostCommentSearchDto()
                        .setPostId(id)
                        .setId(commentId).
                        setIsDeleted(!status))).get(0);
        postComment.setIsDelete(status);
        postCommentRepository.save(postComment);
        PostCommentDto postCommentDto = new PostCommentDto();
        if (status) {
            postCommentDto.setId(postComment.getId());
        } else {
            postCommentDto = mapperComment.postCommentToPostCommentDto(postComment);
        }
        postCommentRs.addPostCommentDto(postCommentDto);
        postCommentRs.setTimestamp(Instant.now().getEpochSecond());
        log.info("Stop - deleteOrRecoveryCommentByIdPost, id =" + id);
        return postCommentRs;
    }

    public CommentComplaintRs addNewCommentComplaint(int id, int commentId) {
        CommentComplaintRs commentComplaintRs = new CommentComplaintRs();
        Post post = postRepository.findById(id).orElseThrow(() -> new NullPointerException());
        PostComment comment = postCommentRepository.findById(commentId).orElseThrow(() -> new NullPointerException());
        if (post.getIsBlocked() || post.getIsDelete() || comment.getIsBlocked() || comment.getIsDelete()) {
            throw new PropertyValueException("", "", "");
        }
        try {
            comment.setIsBlocked(true);
            postCommentRepository.save(comment);
            commentComplaintRs.setTimestamp(Instant.now().getEpochSecond());
            ComplaintRs complaintRs = new ComplaintRs();
            complaintRs.setMessage("ok");
            commentComplaintRs.addPostCommentComplaint(complaintRs);
        } catch (PropertyValueException ex) {
        }
        return commentComplaintRs;
    }

    private void addSubComments(List<PostComment> postCommentList,
                                List<PostCommentDto> postCommentDtoList) {
        for (PostCommentDto postCommentDto : postCommentDtoList) {
            postCommentDto.setSubComment(mapperComment.convertPostCommentListToPostCommentDtoList(
                    postCommentList.stream().filter(c -> c.getParentId() == postCommentDto.getId())
                            .collect(Collectors.toList())));
        }
    }

    private void fillSubComments(List<PostComment> postComment,
                                 List<PostCommentDto> postCommentDtoList,
                                 AccountByIdDto a) {
        postComment.forEach(comment -> {
            if (a.getId().intValue() == comment.getAuthorId()) {
                postCommentDtoList.forEach(commentDto -> {
                    if (commentDto.getId() == comment.getId()) {
                        commentDto.setAuthor(a);
                        commentDto.setPostId(comment.getPost().getId());
                        commentDto.setLikeAmount(postLikeRepository.findAll(getSpecificationLike(
                                new PostLikeSearchDto().setItemId(comment.getId())
                                        .setType(LikeType.COMMENT))).size());
                        commentDto.setMyLike(likeService.getLiked(commentDto.getId(), LikeType.COMMENT)
                                .getData().get(0).getLiked());
                    }
                    commentDto.getSubComment().forEach(subComment -> {
                        if (subComment.getId() == comment.getId()) {
                            subComment.setAuthor(a);
                            subComment.setPostId(comment.getPost().getId());
                            subComment.setLikeAmount(postLikeRepository.findAll(getSpecificationLike(
                                    new PostLikeSearchDto().setItemId(comment.getId())
                                            .setType(LikeType.COMMENT))).size());
                            subComment.setMyLike(likeService.getLiked(subComment.getId(), LikeType.COMMENT)
                                    .getData().get(0).getLiked());
                        }
                    });
                });
            }
        });
    }

    private Specification<PostComment> getSpecificationComment(PostCommentSearchDto dto) {
        log.info("Start - getSpecificationComment");
        return is(PostComment_.id, dto.getId(), true)
                .and(is(PostComment_.post,
                        postRepository.findAll(getSpecification(
                                new PostSearchDto().setId(dto.getPostId()))).get(0), true))
                .and(is(PostComment_.isDelete, dto.getIsDeleted(), true))
                .and(is(PostComment_.isBlocked, dto.getIsBlocked(), true));
    }

    private Specification<Post> getSpecification(PostSearchDto dto) {
        log.info("Start - getSpecification");
        return is(Post_.id, dto.getId(), true)
                .and(is(Post_.isDelete, dto.getIsDeleted(), true))
                .and(is(Post_.isBlocked, dto.getIsBlocked(), true));
    }

    private Specification<PostLike> getSpecificationLike(PostLikeSearchDto dto) {
        log.info("Start - getSpecification");

        return is(PostLike_.itemId, dto.getItemId(), true)
                .and(is(PostLike_.type, dto.getType(), true));
    }
}
