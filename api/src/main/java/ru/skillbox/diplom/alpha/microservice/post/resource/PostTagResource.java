package ru.skillbox.diplom.alpha.microservice.post.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.alpha.microservice.post.request.PostLikeRq;
import ru.skillbox.diplom.alpha.microservice.post.request.PostTagRq;
import ru.skillbox.diplom.alpha.microservice.post.response.PostTagRs;
import ru.skillbox.diplom.alpha.microservice.post.response.TagRs;

/**
 * PostTagResource
 *
 * @author Olga Samoylova
 */

@RequestMapping("/api/v1/tags/")
public interface PostTagResource {
    @GetMapping("")
    ResponseEntity<PostTagRs> getTags(@RequestParam String tag,
                                      @RequestParam(defaultValue = "0") Integer offset,
                                      @RequestParam(defaultValue = "20") Integer itemPerPage);

    @PutMapping("/{postId}")
    ResponseEntity<PostTagRs> putTag(@PathVariable(name = "postId") Integer postId,
                                     @RequestBody PostTagRq request); //поставить тэг

    @DeleteMapping("{postId}/{tagId}")
    ResponseEntity<TagRs> deleteTag(@PathVariable(name = "postId") Integer postId,
                                    @PathVariable(name = "tagId") Integer tagId); //удалить тэг
}
