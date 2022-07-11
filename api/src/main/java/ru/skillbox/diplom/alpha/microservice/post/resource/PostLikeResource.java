package ru.skillbox.diplom.alpha.microservice.post.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.alpha.microservice.post.model.LikeType;
import ru.skillbox.diplom.alpha.microservice.post.request.PostLikeRq;
import ru.skillbox.diplom.alpha.microservice.post.response.ErrorPostRs;
import ru.skillbox.diplom.alpha.microservice.post.response.PostLikeRs;

/**
 * PostLikeResource
 *
 * @author Olga Samoylova
 */


public interface PostLikeResource {
    @GetMapping("/api/v1/liked")
    ResponseEntity<PostLikeRs> getLiked(@RequestParam(name = "item_id")Integer itemId,
                                        @RequestParam(name = "type") LikeType type); //был ли поставлен лайк пользователем

    @GetMapping("/api/v1/likes")
    ResponseEntity<PostLikeRs> getLikes(@RequestParam(name = "item_id")Integer itemId,
                                        @RequestParam(name = "type") LikeType type); //получить список лайков

    @PutMapping("/api/v1/likes")
    ResponseEntity<PostLikeRs> putLike(@RequestBody PostLikeRq request); //поставить лайк

    @DeleteMapping("/api/v1/likes")
    ResponseEntity<PostLikeRs> deleteLike(@RequestBody PostLikeRq request); //снять лайк
}
