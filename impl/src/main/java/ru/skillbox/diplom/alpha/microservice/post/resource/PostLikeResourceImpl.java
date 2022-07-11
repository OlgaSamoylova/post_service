package ru.skillbox.diplom.alpha.microservice.post.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.alpha.microservice.post.model.LikeType;
import ru.skillbox.diplom.alpha.microservice.post.request.PostLikeRq;
import ru.skillbox.diplom.alpha.microservice.post.response.ErrorPostRs;
import ru.skillbox.diplom.alpha.microservice.post.response.PostLikeRs;
import ru.skillbox.diplom.alpha.microservice.post.service.PostLikeService;


/**
 * PostLikeResourceImpl
 *
 * @author Olga Samoylova
 */


@Setter
@Getter
@RestController
@RequiredArgsConstructor
@Tag(name = "likes", description = "Работа с \"Лайками\"")
public class PostLikeResourceImpl implements PostLikeResource {

    private final PostLikeService postLikeService;

    @Override
    @Operation(summary = "Был ли поставлен лайк пользователем")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostLikeRs.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorPostRs.class)),
                    }),
            @ApiResponse(responseCode = "401", description = "unauthorized",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorPostRs.class)),
                    })
    })
    public ResponseEntity<PostLikeRs> getLiked(
            @Parameter(description = "ID объекта у которого необходимо получить \"Лайки\"") Integer itemId,
            @Parameter(description = "Available values : Post, Comment") LikeType type) {
        return ResponseEntity.ok(postLikeService.getLiked(itemId, type));
    }

    @Override
    @Operation(summary = "Получить список пользователей оставивших лайк")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostLikeRs.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorPostRs.class)),
                    }),
            @ApiResponse(responseCode = "401", description = "unauthorized",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorPostRs.class)),
                    })
    })
    public ResponseEntity<PostLikeRs> getLikes(
            @Parameter(description = "ID объекта у которого необходимо получить \"Лайки\"") Integer itemId,
            @Parameter(description = "Available values : Post, Comment")  LikeType type) {
        return ResponseEntity.ok(postLikeService.getLikes(itemId, type));
    }

    @Override
    @Operation(summary = "Поставить лайк")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostLikeRs.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorPostRs.class)),
                    }),
            @ApiResponse(responseCode = "401", description = "unauthorized",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorPostRs.class)),
                    })
    })
    public ResponseEntity<PostLikeRs> putLike(
            @Parameter(schema = @Schema(implementation = PostLikeRq.class)) PostLikeRq request) {
        return ResponseEntity.ok(postLikeService.putLike(request.getItemId(), request.getType()));
    }

    @Override
    @Operation(summary = "Убрать лайк")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostLikeRs.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorPostRs.class)),
                    }),
            @ApiResponse(responseCode = "401", description = "unauthorized",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorPostRs.class)),
                    })
    })
    public ResponseEntity<PostLikeRs> deleteLike(
            @Parameter(schema = @Schema(implementation = PostLikeRq.class)) PostLikeRq request) {
        return ResponseEntity.ok(postLikeService.deleteLike(request.getItemId(), request.getType()));
    }


}
