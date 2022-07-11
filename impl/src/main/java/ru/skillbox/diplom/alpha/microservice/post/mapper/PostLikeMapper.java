package ru.skillbox.diplom.alpha.microservice.post.mapper;

import org.mapstruct.Mapper;
import ru.skillbox.diplom.alpha.microservice.post.dto.PostDto;
import ru.skillbox.diplom.alpha.microservice.post.dto.PostLikeDto;
import ru.skillbox.diplom.alpha.microservice.post.model.Post;
import ru.skillbox.diplom.alpha.microservice.post.model.PostLike;

import java.util.List;

/**
 * PostLikeMapper
 *
 * @author Olga Samoylova
 */

@Mapper
public interface PostLikeMapper {
    PostLike postLikeDtoToPostLike(PostLikeDto entity);
    PostLikeDto postLikeToPostLikeDTO(PostLike entity);
    List<PostLike> convertPostLikeDtoListToPostLikeList(List<PostLikeDto> list);
    List<PostLikeDto> convertPostLikeListToPostLikeDtoList(List<PostLike> list);
}


