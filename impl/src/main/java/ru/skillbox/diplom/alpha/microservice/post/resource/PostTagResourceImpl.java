package ru.skillbox.diplom.alpha.microservice.post.resource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.alpha.microservice.post.request.PostTagRq;
import ru.skillbox.diplom.alpha.microservice.post.response.PostTagRs;
import ru.skillbox.diplom.alpha.microservice.post.response.TagRs;
import ru.skillbox.diplom.alpha.microservice.post.service.PostTagService;

/**
 * PostTagResourceImpl
 *
 * @author Olga Samoylova
 */


@Setter
@Getter
@RestController
@RequiredArgsConstructor
public class PostTagResourceImpl implements PostTagResource{

    private final PostTagService postTagService;

    @Override
    public ResponseEntity<PostTagRs> getTags(@RequestParam String tag,
                                             @RequestParam(defaultValue = "0") Integer offset,
                                             @RequestParam(defaultValue = "20") Integer itemPerPage) {
        return ResponseEntity.ok(postTagService.getTags(tag));
    }

    @Override
    public ResponseEntity<PostTagRs> putTag(Integer postId, PostTagRq request) {
        return ResponseEntity.ok(postTagService.putTag(postId, request));
    }

    @Override
    public ResponseEntity<TagRs> deleteTag(Integer postId, Integer tagId) {
        return ResponseEntity.ok(postTagService.deleteTag(postId, tagId));
    }
}
