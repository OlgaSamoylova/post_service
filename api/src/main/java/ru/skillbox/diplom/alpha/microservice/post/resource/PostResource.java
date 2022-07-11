package ru.skillbox.diplom.alpha.microservice.post.resource;

import org.apache.commons.lang3.exception.ContextedException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.alpha.microservice.post.request.PostAddRq;
import ru.skillbox.diplom.alpha.microservice.post.response.PostComplaintRs;
import ru.skillbox.diplom.alpha.microservice.post.response.PostDefaultRs;
import ru.skillbox.diplom.alpha.microservice.post.response.PostRs;

import java.util.List;

/**
 * PostResource
 *
 * @author Ruslan Akbashev
 */

@RequestMapping("/api/v1/")
public interface PostResource {

    @GetMapping("post/{id}")
    ResponseEntity<PostRs> getPostById(@PathVariable(name = "id") Integer id);

    @PutMapping("post/{id}")
    ResponseEntity<PostRs> putPostById(
            @PathVariable(name = "id") Integer id,
            @RequestParam(required = false, name = "publish_date", defaultValue = "-1") Long publishDate,
            @RequestBody PostAddRq request) throws Exception;

    @DeleteMapping("post/{id}")
    ResponseEntity<PostDefaultRs> deletePostById(@PathVariable(name = "id") Integer id) throws ContextedException;

    @PutMapping("post/{id}/recover")
    ResponseEntity<PostRs> recoveryPostById(@PathVariable(name = "id") Integer id) throws ContextedException;

    @GetMapping("post")
    ResponseEntity<PostRs> getPostAll(@RequestParam(required = false) String text,
                                      @RequestParam(required = false, defaultValue = "-1",
                                              name = "date_from") Long dateFrom,
                                      @RequestParam(required = false, defaultValue = "-1",
                                              name = "date_to") Long dateTo,
                                      @RequestParam(required = false) String author,
                                      @RequestParam(required = false) List<String> tags,
                                      @PageableDefault(sort = "time", direction = Sort.Direction.DESC) Pageable pageable);

    @PostMapping("post")
    ResponseEntity<PostDefaultRs> addNewPost(
            @RequestParam(name =  "publish_date", required = false, defaultValue = "-1") long publishDate,
            @RequestBody PostAddRq request) throws Exception;

    @PostMapping("post/{id}/report")
    ResponseEntity<PostComplaintRs> addNewComplaint(@PathVariable(name = "id") Integer id);

    @GetMapping("feeds")
    ResponseEntity<PostRs> getFeeds(@PageableDefault(sort = "time", direction = Sort.Direction.DESC) Pageable pageable);

    @GetMapping("post/wall")
    ResponseEntity<PostRs> getPostWall();

    @GetMapping("post/wall/{id}")
    ResponseEntity<PostRs> getPostWallById(@PathVariable(name = "id") Integer id);
}