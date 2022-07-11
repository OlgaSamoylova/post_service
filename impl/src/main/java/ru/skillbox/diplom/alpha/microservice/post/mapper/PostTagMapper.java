package ru.skillbox.diplom.alpha.microservice.post.mapper;

import org.mapstruct.Mapper;
import ru.skillbox.diplom.alpha.microservice.post.dto.PostDto;
import ru.skillbox.diplom.alpha.microservice.post.dto.PostTagDto;
import ru.skillbox.diplom.alpha.microservice.post.model.Post;
import ru.skillbox.diplom.alpha.microservice.post.model.PostTag;

import java.util.List;

/**
 * PostTagMapper
 *
 * @author Olga Samoylova
 */

@Mapper
public interface PostTagMapper {
    PostTag postTagDtoToPostTag (PostTagDto entity);
    PostTagDto postTagToPostTagDto(PostTag entity);

    List<PostTag> convertPostTagDtoListToPostTagList(List<PostTagDto> list);
    List<PostTagDto> convertPostTagListToPostTagDtoList(List<PostTag> list);
}
