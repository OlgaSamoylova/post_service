package ru.skillbox.diplom.alpha.microservice.post.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.skillbox.diplom.alpha.microservice.post.dto.PostDto;
import ru.skillbox.diplom.alpha.microservice.post.model.Post;

import javax.xml.stream.events.Comment;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PostMapper
 *
 * @author Ruslan Akbashev
 */

@Mapper
public interface PostMapper {
    Post postDtoToPost(PostDto entity);

    PostDto postToPostDto(Post entity);

    List<Post> convertPostDtoListToPostList(List<PostDto> list);

    List<PostDto> convertPostListToPostDtoList(List<Post> list);
}
