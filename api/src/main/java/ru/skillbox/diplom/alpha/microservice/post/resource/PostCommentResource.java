package ru.skillbox.diplom.alpha.microservice.post.resource;

import org.apache.commons.lang3.exception.ContextedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.alpha.microservice.post.request.CommentAddRq;
import ru.skillbox.diplom.alpha.microservice.post.response.CommentComplaintRs;
import ru.skillbox.diplom.alpha.microservice.post.response.PostCommentRs;

/**
 * PostCommentResource
 *
 * @author Ruslan Akbashev
 */
@RequestMapping("/api/v1/post/")
public interface PostCommentResource {

    @GetMapping("{id}/comments")
    ResponseEntity<PostCommentRs> getCommentByIdPost(
            @PathVariable(name = "id") Integer id,
            @RequestParam (required = false, defaultValue = "0") Integer offset,
            @RequestParam (required = false, defaultValue = "20", name = "perPage")Integer itemPerPage);

    @PostMapping("{id}/comments")
    ResponseEntity<PostCommentRs> addCommentByIdPost(
            @PathVariable(name = "id") Integer id,
            @RequestBody CommentAddRq request);

    @PutMapping("{id}/comments/{comment_id}")
    ResponseEntity<PostCommentRs> putCommentByIdPost(
            @PathVariable(name = "id") Integer id, @PathVariable(name = "comment_id") Integer commentId,
            @RequestBody CommentAddRq request) throws ContextedException;

    @DeleteMapping("{id}/comments/{comment_id}")
    ResponseEntity<PostCommentRs> deleteCommentByIdPost(
            @PathVariable(name = "id") Integer id, @PathVariable(name = "comment_id") Integer commentId);

    @PutMapping("{id}/comments/{comment_id}/recover")
    ResponseEntity<PostCommentRs> recoveryCommentByIdPost(
            @PathVariable(name = "id") Integer id, @PathVariable(name = "comment_id") Integer commentId);

    @PostMapping("{id}/comments/{comment_id}/report")
    ResponseEntity<CommentComplaintRs> addCommentComplaint(
            @PathVariable(name = "id") Integer id, @PathVariable(name = "comment_id") Integer commentId);

}
