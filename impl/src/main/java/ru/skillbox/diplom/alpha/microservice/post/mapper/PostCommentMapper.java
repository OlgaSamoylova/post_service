package ru.skillbox.diplom.alpha.microservice.post.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.skillbox.diplom.alpha.microservice.post.dto.PostCommentDto;
import ru.skillbox.diplom.alpha.microservice.post.model.PostComment;

import java.util.List;

/**
 * PostCommentMapper
 *
 * @author Ruslan Akbashev
 */
@Mapper
public interface PostCommentMapper {
//    @Mappings({@Mapping(target = "id" , source = "entity.id"),
//            @Mapping(target = "time" , source = "entity.time"),
//            @Mapping(target = "parentId" , source = "entity.parentId"),
//            @Mapping(target = "commentText" , source = "entity.commentText"),
//            @Mapping(target = "post.id" , source = "entity.postId"),
//            @Mapping(target = "authorId" , source = "entity.authorId"),
//            @Mapping(target = "isBlocked" , source = "entity.isBlocked")})
    PostComment postCommentDtoToPostComment(PostCommentDto entity);

//    @Mappings({@Mapping(target = "id" , source = "entity.id"),
//            @Mapping(target = "time" , source = "entity.time"),
//            @Mapping(target = "parentId" , source = "entity.parentId"),
//            @Mapping(target = "commentText" , source = "entity.commentText"),
//            @Mapping(target = "postId" , source = "entity.post.id"),
//            @Mapping(target = "authorId" , source = "entity.authorId"),
//            @Mapping(target = "isBlocked" , source = "entity.isBlocked")})
    PostCommentDto postCommentToPostCommentDto(PostComment entity);

    List<PostComment> convertPostCommentDtoListToPostCommentList(List<PostCommentDto> list);

    List<PostCommentDto> convertPostCommentListToPostCommentDtoList(List<PostComment> list);
}
