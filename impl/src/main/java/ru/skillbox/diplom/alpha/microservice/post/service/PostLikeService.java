package ru.skillbox.diplom.alpha.microservice.post.service;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.mapstruct.factory.Mappers;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.alpha.microservice.post.dto.PostLikeDto;
import ru.skillbox.diplom.alpha.microservice.post.dto.PostLikeSearchDto;
import ru.skillbox.diplom.alpha.microservice.post.mapper.PostLikeMapper;
import ru.skillbox.diplom.alpha.microservice.post.model.*;
import ru.skillbox.diplom.alpha.microservice.post.model.Post;
import ru.skillbox.diplom.alpha.microservice.post.repository.PostCommentRepository;
import ru.skillbox.diplom.alpha.microservice.post.repository.PostLikeRepository;
import ru.skillbox.diplom.alpha.microservice.post.repository.PostRepository;
import ru.skillbox.diplom.alpha.microservice.post.response.LikeRs;
import ru.skillbox.diplom.alpha.microservice.post.response.PostLikeRs;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static ru.skillbox.diplom.alpha.library.core.repository.NewSpecification.is;
import static ru.skillbox.diplom.alpha.library.core.security.utils.SecurityUtils.getAccountId;

/**
 * PostLikeService
 *
 * @author Olga Samoylova
 */

@Setter
@Getter
@Service
@Slf4j
@RequiredArgsConstructor
public class PostLikeService {

    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostLikeMapper postLikeMapper = Mappers.getMapper(PostLikeMapper.class);


    public PostLikeRs getLiked(int itemId, LikeType type){
        log.info("start get liked");
        PostLikeRs response = getResponseToGetLiked(itemId, type);
        log.info("finish get liked");
        return response;
    }

    private PostLikeRs getResponseToGetLiked(int itemId, LikeType type){
        log.info("start get response to get liked");
        PostLike like = findLike(itemId, type);
        PostLikeRs response = new PostLikeRs();
        List<LikeRs> data = new ArrayList<>();
        LikeRs likeRs = new LikeRs();
        if (like != null) {
            likeRs.setLiked(true);
        } else {
            likeRs.setLiked(false);
        }
        likeRs.setLikes(null);
        data.add(likeRs);
        response.setData(data);
        response.setTimestamp(Instant.now().getEpochSecond());
        log.info("finish get response to get liked");
        return response;
    }

    public PostLikeRs getLikes(int itemId, LikeType type){
        log.info("start get likes list");
        PostLikeRs response = getResponse(itemId, type);
        log.info("finish get likes list");
        return response;
    }

    public PostLikeRs putLike(int itemId, LikeType type){
        log.info("start new like");
        if (checkLike(itemId, type)){
            createLike(itemId, type);
        }
        log.info("finish new like");
        return getResponse(itemId, type);
    }

    public PostLikeRs deleteLike(int itemId, LikeType type){
        log.info("start delete like");
        PostLike like = findLike(itemId, type);
        System.out.println("like " + like);
        if (like != null){
            postLikeRepository.deleteById(like.getId());
            changeLikeAmount(itemId, type, -1);
        }

        PostLikeRs response = getResponse(itemId, type);
        log.info("finish delete like");
        return response;
    }

    public PostLikeRs getResponse(int itemId, LikeType type){
        log.info("start response formation");
        PostLikeRs response = new PostLikeRs();
        response.setTimestamp(Instant.now().getEpochSecond());
        PostLikeSearchDto dto = new PostLikeSearchDto();
        dto.setAuthorId(getAccountId().intValue());
        dto.setItemId(itemId);
        dto.setType(type);
        List<PostLike> postLikeList = postLikeRepository.findAll(getSpecification(dto));
        List<PostLikeDto> likeListDto = postLikeMapper
                .convertPostLikeListToPostLikeDtoList(postLikeList);
        System.out.println("post like list Dto size == " + likeListDto.size());
        List<LikeRs> data = new ArrayList<>();
        LikeRs likeRs = new LikeRs();
        likeRs.setLikes(likeListDto.size());
        likeRs.setUsers(getUsers(postLikeList));
        data.add(likeRs);
        response.setData(data);
        log.info("finish response formation");
        return response;
    }

    private List<Integer> getUsers(List<PostLike> likeList){
        List<Integer> users = new ArrayList<>(likeList.size());
        for (PostLike like : likeList){
            users.add(like.getAuthorId().intValue());
        }
        return users;
    }

    private void createLike(int itemId, LikeType type){
        log.info("start create like");
        PostLike postLike = new PostLike();
        postLike.setTime(Instant.now().getEpochSecond());
        postLike.setAuthorId(getAccountId().intValue());
        postLike.setItemId(itemId);
        postLike.setType(type);
        postLikeRepository.save(postLike);
        changeLikeAmount(itemId, type, 1);
        System.out.println("new like is created id: " + postLike.getId());
        log.info("finish create like");
    }

    private boolean checkLike(int itemId, LikeType type) {
        log.info("start check like");
        PostLikeSearchDto dto = new PostLikeSearchDto(getAccountId().intValue(), itemId, type);
        List<PostLike> postLikeList = postLikeRepository.findAll(getSpecificationAuthor(dto));
        if (postLikeList.size() == 0){
            log.info("finish check like value TRUE");
            return true;
        }
        else {
            log.info("finish check like value FALSE");
            return false;
        }
    }

    private PostLike findLike(int itemId, LikeType type){
        log.info("start find like");
        PostLikeSearchDto dto = new PostLikeSearchDto(getAccountId().intValue(), itemId, type);
        List<PostLike> likeList = postLikeRepository.findAll(getSpecificationAuthor(dto));
        System.out.println("like list size " + likeList.size());
        if (likeList.size() == 0){
            log.info("finish find like with value null");
            return null;
        }
        else {
            log.info("finish find like with value " + likeList.get(0));
            return likeList.get(0);
        }
    }

    private boolean checkItem(int itemId, LikeType type){
        log.info("start check item");
        boolean itemExist = false;
        if (type == LikeType.POST){
            Post post = postRepository.findById(itemId).orElseThrow(NoSuchFieldError :: new);
            itemExist = true;
        }
        if (type == LikeType.COMMENT){
            PostComment comment = postCommentRepository.findById(itemId).orElseThrow(NoSuchFieldError :: new);
            itemExist = true;
        }
        log.info("finish check item");
        return itemExist;
    }

    private Specification<PostLike> getSpecificationAuthor(PostLikeSearchDto dto) {
        log.info("Start - getSpecificationAuthor");

        return is(
                PostLike_.authorId, dto.getAuthorId().intValue(), true)
                .and(is(PostLike_.itemId, dto.getItemId(), true))
                .and(is(PostLike_.type, dto.getType(), true));
    }

    private Specification<PostLike> getSpecification(PostLikeSearchDto dto) {
        log.info("Start - getSpecification");

        return is(PostLike_.itemId, dto.getItemId(), true)
                .and(is(PostLike_.type, dto.getType(), true));
    }

    private void changeLikeAmount(int itemId, LikeType type, int i){
        log.info("Start change like amount");
        if (type == LikeType.POST){
            Post post = postRepository.findById(itemId).get();
            post.setLikeAmount(post.getLikeAmount()+i);
            postRepository.save(post);
            System.out.println("like amount = " + post.getLikeAmount());
        }
        else if (type == LikeType.COMMENT){
            PostComment comment = postCommentRepository.findById(itemId).get();
            comment.setLikeAmount(comment.getLikeAmount()+i);
            postCommentRepository.save(comment);
            System.out.println("like amount = " + comment.getLikeAmount());
        }
        log.info("finish change like amount");
    }

}
