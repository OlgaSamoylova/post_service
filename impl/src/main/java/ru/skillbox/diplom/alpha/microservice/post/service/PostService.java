package ru.skillbox.diplom.alpha.microservice.post.service;

import feign.Client;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ContextedException;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.alpha.library.core.config.TechnicalUserConfig;
import ru.skillbox.diplom.alpha.library.core.dto.AccountByIdDto;
import ru.skillbox.diplom.alpha.library.core.dto.AccountSearchDto;
import ru.skillbox.diplom.alpha.microservice.account.resources.FeignAccountResource;
import ru.skillbox.diplom.alpha.microservice.friend.resources.FeignFriendResource;
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
import ru.skillbox.diplom.alpha.microservice.post.repository.PostTagRepository;
import ru.skillbox.diplom.alpha.microservice.post.request.PostAddRq;
import ru.skillbox.diplom.alpha.microservice.post.response.*;

import java.net.http.HttpClient;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.skillbox.diplom.alpha.library.core.repository.NewSpecification.*;
import static ru.skillbox.diplom.alpha.library.core.security.utils.SecurityUtils.getAccountId;

/**
 * PostService
 *
 * @author Ruslan Akbashev, Olga Samoylova
 */
@Setter
@Getter
@Service
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
public class PostService {
    @Value("${final.title.length}")
    private int titleLength;

    @Value("${final.text.length}")
    private int textLength;

    @Value("${final.default.date}")
    private int defaultDate;

    private final FeignAccountResource feignAccountResource;

    private final FeignFriendResource feignFriendResource;

    private final FeignNotificationResource feignNotificationResource;

    private final PostRepository postRepository;

    private final PostCommentRepository postCommentRepository;

    private final PostTagService postTagService;

    private final PostTagRepository postTagRepository;

    private final PostLikeRepository postLikeRepository;

    private final PostLikeService likeService;

    private final StoragePostService storagePostService;

    private final TechnicalUserConfig technicalUserConfig;

    PostMapper mapper = Mappers.getMapper(PostMapper.class);
    PostCommentMapper mapperComment = Mappers.getMapper(PostCommentMapper.class);

    public PostRs getPostAll(String text,
                             long dateFrom,
                             long dateTo,
                             String author,
                             List<String> tags,
                             Pageable pageable) {
        log.info("Start - getPostAll, text = " + text
                + ", dateFrom = " + dateFrom
                + ", dateTo = " + dateTo
                + ", author = " + author
                + ", page = " + pageable.getPageNumber()
                + ", size = " + pageable.getPageSize());
        List<Integer> accountIdList = new ArrayList<>();
        if (author != null) {
            accountIdList = feignAccountResource.getIdsByAuthor(author)
                    .stream().map(Long::intValue).collect(Collectors.toList());
            log.info("account list - " + accountIdList);
        }
        if (author != null
                && (feignAccountResource.getById(getAccountId()).getFirstName().equals(author)
                || feignAccountResource.getById(getAccountId()).getLastName().equals(author))){
            accountIdList.add(getAccountId().intValue());
            log.info("добавляем свой аккаунт, если имя или фамилия совпали с искомым автором " + accountIdList);
        }
        PostRs postRs = new PostRs();
        log.info("начинаем искать посты по тэгам");
        Set<Integer> postIds = new HashSet<>();
        List<Integer> postIdsList = new ArrayList<>();
        if (tags != null){
            Set<String> newTags = new HashSet<>(tags);
            for (String tag: newTags){
                List<PostDto> postDtoList = postTagService.getTags(tag).getDataPosts();
                for (PostDto dto: postDtoList){
                        postIds.add(dto.getId());
                }
            }
            postIdsList = new ArrayList<>(postIds);
        }
        log.info("закончили искать посты по тэгам");

        log.info("формируем Dto для поиска постов");
        PostSearchDto searchDto = new PostSearchDto()
                .setPostText(text)
                .setTitle(text)
                .setTimeTo(dateTo > -1 ? dateTo : Instant.now().getEpochSecond())
                .setTimeFrom(dateFrom > -1 ? dateFrom : null)
                .setIsBlocked(false).setIsDeleted(false);

        if (author != null){
            searchDto.setAuthorListId(accountIdList);
        }
        if ((tags != null)&&(!postIdsList.isEmpty())) {
            searchDto.setPostListId(postIdsList);
        }
        log.info("Dto для поиска постов: " + searchDto.toString());

        log.info("начинаем искать посты по другим параметрам");
        Page<Post> postList = postRepository.findAll(getSpecification1(searchDto), pageable);

        log.info("нашли все нужные посты -" + postList.getTotalElements());
        convertToPostDtoList(postRs, postList.getContent());
        postRs.setTimestamp(Instant.now().getEpochSecond());
        postRs.setPage(postList.getNumber());
        postRs.setSize(postList.getContent().size());
        postRs.setTotal((int) postList.getTotalElements());
        log.info("Stop - getPostAll");
        return postRs;
    }

    public PostDefaultRs addNewPost(long publishDate, PostAddRq request) throws Exception {
        log.info("Start - addNewPost + title - " + request.getTitle() + ", post_text - " + request.getPostText()
                + ", publish_date = " + publishDate);
        lengthCheck(request, publishDate);
        PostDefaultRs postDefaultRs = new PostDefaultRs();
        Post post = new Post();
        post.setTime(Instant.now().getEpochSecond() >= publishDate ?
                Instant.now().getEpochSecond() : publishDate);
        post.setAuthorId(Math.toIntExact(getAccountId()));
        post.setPostText(request.getPostText());
        post.setTitle(request.getTitle());
        post.setIsDelete(false);
        post.setIsBlocked(false);
        checkTags(request.getTags(), post);
        post.setLikeAmount(0);
        post.setImagePath(request.getPhotoUrl());
        postRepository.save(post);
        MessageRs messageRs = new MessageRs();
        messageRs.setMessage("true");
        postDefaultRs.setTimestamp(Instant.now().getEpochSecond());
        postDefaultRs.setData(messageRs);
        if (!(post.getTime() > Instant.now().getEpochSecond())){
            NotificationInputDto notificationInputDto = new NotificationInputDto();
            notificationInputDto.setUserId(getAccountId());
            notificationInputDto.setNameNotification(NotificationType.POST);
            notificationInputDto.setContent(post.getTitle());
            feignNotificationResource.createNotification(notificationInputDto);
        }
        log.info("Stop - addNewPost. authorId - " + getAccountId() +", post_title - " + post.getTitle());
        return postDefaultRs;
    }


    @Scheduled(cron = "@hourly")
    private void timeToPost(){
        long time = Instant.now().getEpochSecond();
        log.info("нотификация о публикации отложенного поста " + time);
        List<Post> posts = technicalUserConfig.executeByTechnicalUser(() ->
                postRepository.findAll(getSpecification(new PostSearchDto().setTimeFrom(time-3000)
                        .setTimeTo(time+3000))));
        int postCount = 0;
        for (Post post: posts){
                NotificationInputDto notificationInputDto = new NotificationInputDto();
                notificationInputDto.setUserId(post.getAuthorId().longValue());
                log.info("authorId " + post.getAuthorId());
                notificationInputDto.setNameNotification(NotificationType.POST);
                notificationInputDto.setContent(post.getTitle());
                postCount++;
                        technicalUserConfig.executeByTechnicalUser(() -> {
                        feignNotificationResource.createNotification(notificationInputDto);
                        return null;
                        });

        }
        log.info("создано " + postCount + " постов и отправлены нотификации " + posts.toString());

    }

    public PostRs getPostById(int id) {
        log.info("Start - getPostById, id =" + id);
        PostRs postRs = new PostRs();
        Post post = postRepository.findAll(getSpecification(new PostSearchDto()
                .setId(id).setIsBlocked(false).setIsDeleted(false)
                .setTimeTo(Instant.now().getEpochSecond()))).get(0);
        fillingResponse(postRs, post);
        log.info("Stop - getPostById, id =" + id);
        return postRs;
    }

    private void fillingResponse(PostRs postRs, Post post) {
        log.info("Start - fillingResponse");
        PostDto postDto = mapper.postToPostDto(post);
        post.getCommentsList();
        postDto.setPostCommentList(
                mapperComment.convertPostCommentListToPostCommentDtoList(
                        post.getCommentList().stream()
                                .filter(c -> c.getParentId() == null).collect(Collectors.toList())));
        addSubComments(post, postDto);
        Set<Long> accountIdList = new HashSet<>();
        accountIdList.add(post.getAuthorId().longValue());
        post.getCommentList().forEach(postComment -> accountIdList.add(Long.valueOf(postComment.getAuthorId())));
        List<AccountByIdDto> accountByIdDtoList = feignAccountResource.getAll(
                new AccountSearchDto().setIds(accountIdList.stream().collect(Collectors.toList())));
        accountByIdDtoList.forEach(accountByIdDto -> {
            if (post.getAuthorId() == accountByIdDto.getId().intValue()) {
                postDto.setAuthor(accountByIdDto);
            }
            fillSubComments(post, postDto, accountByIdDto);
        });

        postRs.setTimestamp(Instant.now().getEpochSecond());
        postDto.setLikeAmount(postLikeRepository.findAll(getSpecificationLike(
                new PostLikeSearchDto().setItemId(post.getId())
                        .setType(LikeType.POST))).size());
        postDto.setType(postDto.getTime() > Instant.now().getEpochSecond() ?
                PostType.QUEUED : PostType.POSTED);
        postDto.setMyLike(likeService.getLiked(postDto.getId(), LikeType.POST).getData().get(0).getLiked());
        postRs.addPostDto(postDto);
        log.info("Stop - fillingResponse");
    }

    public PostRs putPostById(int id, long publishDate, PostAddRq request) throws Exception {
        log.info("Start - putPostById, id = " + id + ", title - " + request.getTitle() + ", post_text - " + request.getPostText());
        PostRs postRs = new PostRs();
        Post post = postRepository.findAll(getSpecification(new PostSearchDto().setId(id)
                .setIsBlocked(false).setIsDeleted(false))).get(0);
        if (post.getAuthorId() != getAccountId().intValue()) {
            log.info("Пользователь не является автором поста");
            throw new ContextedException("Пользователь не является автором поста");
        }
        lengthCheck(request, publishDate);
        post.setPostText(request.getPostText());
        post.setTitle(request.getTitle());
        post.setTime(publishDate > Instant.now().getEpochSecond() ?
                publishDate : Instant.now().getEpochSecond());

        checkTags(request.getTags(), post);
        post.setImagePath(request.getPhotoUrl());
        postRepository.save(post);
        fillingResponse(postRs, post);
        log.info("Stop - putPostById, id = " + id);
        return postRs;
    }

    private void checkTags(Set<String> tags, Post post){
        Set<String> postTagsList = new HashSet<>();
        post.setTags(new HashSet<>());
        if (tags != null) {
            tags.forEach(tag -> postTagsList.add(tag.toLowerCase()));
            for (String tag : tags) {
                if (!postTagRepository.findAll(getSpecificationTag(
                        new PostTagSearchDto().setTag(tag))).isEmpty()) {
                    PostTag postTag = postTagRepository.findAll(
                            getSpecificationTag(new PostTagSearchDto().setTag(tag))).get(0);
                    if(!post.getTags().contains(postTag)){
                        post.addNewTag(postTag);
                    }
                } else {
                    PostTag postTag = new PostTag(tag);
                    postTagRepository.save(postTag);
                    post.addNewTag(postTag);
                }
            }
        }
    }

    public PostDefaultRs deletePostById(int id) throws ContextedException {
        log.info("Start - deletePostById, id = " + id);
        PostDefaultRs postDeleteRs = new PostDefaultRs();
        log.info("Поиск поста для удаления с id = " + id);
        Post post = postRepository.findAll(getSpecification(new PostSearchDto().setId(id)
                .setIsBlocked(false))).get(0);
        if (post.getAuthorId() != getAccountId().intValue()) {
            log.info("Пользователь не является автором поста");
            throw new ContextedException("Пользователь не является автором поста");
        }
        post.setIsDelete(true);
        log.info("Установка статуса удален у комментариев к посту с id = " + id);
        List<PostComment> postCommentList = post.getCommentList();
        if (postCommentList.size() > 0) {
            for (PostComment postComment : postCommentList) {
                postComment.setIsDelete(true);
                postComment.setPost(post);
            }
            log.info("Сохранение статуса удален у поста - " + id);
            postCommentRepository.saveAll(postCommentList);
        }
        postRepository.save(post);
        log.info("Отправка сообщения об удалении поста - " + id);
        MessageRs messageRs = new MessageRs();
        messageRs.setMessage("ok");
        postDeleteRs.setTimestamp(Instant.now().getEpochSecond());
        postDeleteRs.setData(messageRs);
        log.info("Stop - deletePostById, id = " + id);
        return postDeleteRs;
    }

    public PostRs recoveryPostById(int id) throws ContextedException {
        log.info("Start - recoveryPostById, id = " + id);
        PostRs postRs = new PostRs();
        Post post = postRepository.findAll(getSpecification(
                new PostSearchDto().setId(id).setIsDeleted(true).setIsBlocked(false))).get(0);
        if (post.getAuthorId() != getAccountId().intValue()) {
            log.info("Пользователь не является автором поста");
            throw new ContextedException("Пользователь не является автором поста");
        }
        post.setIsDelete(false);
        List<PostComment> postCommentList = post.getCommentList();
        if (postCommentList.size() > 0) {
            for (PostComment postComment : postCommentList) {
                postComment.setIsDelete(false);
                postComment.setPost(post);
            }
            postCommentRepository.saveAll(postCommentList);
        }
        postRepository.save(post);
        fillingResponse(postRs, post);
        log.info("Stop - recoveryPostById, id = " + id);
        return postRs;
    }

    public PostComplaintRs addNewComplaint(int id) {
        PostComplaintRs postComplaintRs = new PostComplaintRs();
        Post post = postRepository.findById(id).orElseThrow(() -> new NullPointerException());
        post.setIsBlocked(true);
        postComplaintRs.setTimestamp(Instant.now().getEpochSecond());
        postRepository.save(post);
        ComplaintRs complaintRs = new ComplaintRs();
        complaintRs.setMessage("ok");
        postComplaintRs.addPostComplaint(complaintRs);
        return postComplaintRs;
    }

    public PostRs getFeeds(Pageable pageable) {
        log.info("Start - getFeeds, page = " + pageable.getPageNumber()
                + ", size = " + pageable.getPageSize());
        List<Integer> friendList = null;
        if (!feignFriendResource.getAllFriendIds(getAccountId()).isEmpty()) {
            friendList = feignFriendResource.getAllFriendIds(getAccountId()).stream().map(Long::intValue)
                    .collect(Collectors.toList());
        }
        PostRs postRs = new PostRs();
        Page<Post> postList = postRepository.findAll(getSpecification(
                new PostSearchDto().setIsBlocked(false).setTimeTo(Instant.now().getEpochSecond())
                        .setAuthorId(getAccountId().intValue()).setAuthorListId(friendList)
                        .setIsDeleted(false)), pageable);
        convertToPostDtoList(postRs, postList.getContent());
        postRs.setTimestamp(Instant.now().getEpochSecond());
        postRs.setPage(postList.getNumber());
        postRs.setSize(postList.getContent().size());
        postRs.setTotal((int) postList.getTotalElements());
        log.info("Stop - getFeeds");
        return postRs;
    }

    public PostRs getPostWall() {
        log.info("Start - getPostWall");
        PostRs postRs = new PostRs();
        List<Post> postList = postRepository.findAll(getSpecification(
                new PostSearchDto().setAuthorId(getAccountId().intValue())),
                Sort.by("time").descending());
        System.out.println("-------------" + postList.size());
        convertToPostDtoList(postRs, postList);
        postRs.setTimestamp(Instant.now().getEpochSecond());
        postRs.setPage(0);
        postRs.setSize(postList.size());
        postRs.setTotal(postList.size());
        log.info("Stop - getPostWall");
        return postRs;
    }

    public PostRs getPostWallById(Integer userId) {
        log.info("Start - getPostWallById by userId - " + userId);
        PostRs postRs = new PostRs();
        List<Post> postList = postRepository.findAll(getSpecification(new PostSearchDto()
                .setAuthorId(userId).setIsBlocked(false).setTimeTo(Instant.now().getEpochSecond())
                .setIsDeleted(false)), Sort.by("time").descending());
        convertToPostDtoList(postRs, postList);
        postRs.setTimestamp(Instant.now().getEpochSecond());
        postRs.setPage(0);
        postRs.setSize(postList.size());
        postRs.setTotal(postList.size());
        log.info("Stop - getPostWallById by userId - " + userId);
        return postRs;
    }

    private void convertToPostDtoList(PostRs postRs, List<Post> postList) {
        List<PostDto> postDtoList = mapper.convertPostListToPostDtoList(postList);
        Set<Long> accountIdList = new HashSet<>();
        postList.forEach(p ->
        {
            p.getCommentsList();
            accountIdList.add(Long.valueOf(p.getAuthorId()));
            p.getCommentList().forEach(postComment ->
                    accountIdList.add(Long.valueOf(postComment.getAuthorId())));
        });
        List<AccountByIdDto> accountByIdDtoList = feignAccountResource.getAll(
                new AccountSearchDto().setIds(accountIdList.stream().collect(Collectors.toList())));
        for (int i = 0; i < postList.size(); i++) {
            postList.get(i).getCommentsList();
            postDtoList.get(i).setPostCommentList(
                    mapperComment.convertPostCommentListToPostCommentDtoList(
                            postList.get(i).getCommentList().stream().filter(c -> c.getParentId() == null)
                                    .collect(Collectors.toList())));
            addSubComments(postList.get(i), postDtoList.get(i));
            int finalI = i;
            accountByIdDtoList.forEach(a -> {
                if (a.getId().intValue() == postList.get(finalI).getAuthorId()) {
                    postDtoList.get(finalI).setAuthor(a);
                }
                fillSubComments(postList.get(finalI), postDtoList.get(finalI), a);
            });
            postDtoList.get(i).setLikeAmount(postLikeRepository.findAll(getSpecificationLike(
                    new PostLikeSearchDto().setItemId(postDtoList.get(i).getId())
                            .setType(LikeType.POST))).size());
            postDtoList.get(i).setType(postDtoList.get(i).getTime() > Instant.now().getEpochSecond() ?
                    PostType.QUEUED : PostType.POSTED);
        }
        myLikes(postDtoList);
        postRs.setData(postDtoList);
    }

    private void addSubComments(Post post, PostDto postDto) {
        for (PostCommentDto postCommentDto : postDto.getPostCommentList()) {
            postCommentDto.setSubComment(mapperComment.convertPostCommentListToPostCommentDtoList(
                    post.getCommentList().stream().filter(c -> c.getParentId() == postCommentDto.getId())
                            .collect(Collectors.toList())));
            postCommentDto.getSubComment().forEach(subComment -> {
                mapperComment.convertPostCommentListToPostCommentDtoList(
                        post.getCommentList().stream().filter(c -> c.getParentId() == postCommentDto.getId())
                                .collect(Collectors.toList()));
            });
        }
    }

    private void fillSubComments(Post postList, PostDto postDto, AccountByIdDto a) {
        postList.getCommentList().forEach(postComment -> {
            if (a.getId().intValue() == postComment.getAuthorId()) {
                postDto.getPostCommentList().forEach(postCommentDto -> {
                    if (postCommentDto.getId().equals(postComment.getId())) {
                        postCommentDto.setAuthor(a);
                        postCommentDto.setPostId(postComment.getPost().getId());
                        postCommentDto.setLikeAmount(postLikeRepository.findAll(getSpecificationLike(
                                new PostLikeSearchDto().setItemId(postComment.getId())
                                        .setType(LikeType.COMMENT))).size());
                        postCommentDto.setMyLike(likeService.getLiked(postCommentDto.getId(), LikeType.COMMENT).getData().get(0).getLiked());
                    }
                    postCommentDto.getSubComment().forEach(subComment -> {
                        if (subComment.getId().equals(postComment.getId())) {
                            subComment.setAuthor(a);
                            subComment.setPostId(postComment.getPost().getId());
                            subComment.setLikeAmount(postLikeRepository.findAll(getSpecificationLike(
                                    new PostLikeSearchDto().setItemId(postComment.getId())
                                            .setType(LikeType.COMMENT))).size());
                            subComment.setMyLike(likeService.getLiked(subComment.getId(), LikeType.COMMENT).getData().get(0).getLiked());
                        }
                    });
                });
            }
        });
    }

    private void myLikes(List<PostDto> postDtoList) {
        log.info("определяем ставили уже лайк или нет");
        for (PostDto postDto : postDtoList) {
            postDto.setMyLike(likeService.getLiked(postDto.getId(), LikeType.POST).getData().get(0).getLiked());
        }
        log.info("определили ставили уже лайк или нет");
    }

    private void lengthCheck(PostAddRq request, long publishDate) throws Exception {
        if (request.getTitle().length() < titleLength) {
            throw new ContextedException("The length of the post title is less than 3.");
        }
        if (request.getPostText().length() < textLength) {
            throw new ContextedException("The length of the post text is less than 5.");
        }
        if ((publishDate < Instant.now().getEpochSecond()) &&
                (publishDate > defaultDate)) {
            throw new ExceptionInInitializerError("The scheduled date cannot be less than " +
                    "the current date.");
        }
    }

    private Specification<Post> getSpecification(PostSearchDto dto) {
        log.info("Start - getSpecification");
        return is(Post_.id, dto.getId(), true)
                .and(like(Post_.postText, dto.getPostText(), true)
                        .or(like(Post_.title, dto.getTitle(), true)))
                .and(between(Post_.time, dto.getTimeFrom(), dto.getTimeTo()))
                .and(is(Post_.authorId, dto.getAuthorId(), true)
                        .or(in(Post_.authorId, dto.getAuthorListId(), true)))
                .and(is(Post_.isDelete, dto.getIsDeleted(), true))
                .and(is(Post_.isBlocked, dto.getIsBlocked(), true));
    }

    private Specification<Post> getSpecification1(PostSearchDto dto) {
        log.info("Start - getSpecification");
        return in(Post_.id, dto.getPostListId(), true)
                .and(like(Post_.postText, dto.getPostText(), true)
                        .or(like(Post_.title, dto.getTitle(), true)))
                .and(between(Post_.time, dto.getTimeFrom(), dto.getTimeTo()))
                .and(is(Post_.authorId, dto.getAuthorId(), true)
                        .or(in(Post_.authorId, dto.getAuthorListId(), true)))
                .and(is(Post_.isDelete, dto.getIsDeleted(), true))
                .and(is(Post_.isBlocked, dto.getIsBlocked(), true));
    }

    private Specification<PostTag> getSpecificationTag(PostTagSearchDto dto) {
        log.info("Start - getSpecification");
        return is(PostTag_.id, dto.getId(), true)
                .and(is(PostTag_.tag, dto.getTag(), true));
    }

    private Specification<PostLike> getSpecificationLike(PostLikeSearchDto dto) {
        log.info("Start - getSpecification");

        return is(PostLike_.itemId, dto.getItemId(), true)
                .and(is(PostLike_.type, dto.getType(), true));
    }
}