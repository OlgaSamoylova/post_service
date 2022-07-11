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
import org.apache.commons.lang3.exception.ContextedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.alpha.microservice.post.request.CommentAddRq;
import ru.skillbox.diplom.alpha.microservice.post.response.CommentComplaintRs;
import ru.skillbox.diplom.alpha.microservice.post.response.ErrorPostRs;
import ru.skillbox.diplom.alpha.microservice.post.response.PostCommentRs;
import ru.skillbox.diplom.alpha.microservice.post.service.PostCommentService;

/**
 * PostCommentResourceImpl
 *
 * @author Ruslan Akbashev
 */
@Setter
@Getter
@RestController
@RequiredArgsConstructor
@Tag(name = "posts", description = "Работа с публикациями")
public class PostCommentResourceImpl implements PostCommentResource {

    private final PostCommentService postCommentService;

    private final Boolean DELETE_COMMENT = true;

    private final Boolean RECOVERY_COMMENT = false;

    @Override
    @Operation(summary = "Получение комментариев на публикации")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение комментариев",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostCommentRs.class))
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
    public ResponseEntity<PostCommentRs> getCommentByIdPost(
            @Parameter(description = "ID публикации") Integer id,
            @Parameter(description = "Отступ от начала списка") Integer offset,
            @Parameter(description = "Количество элементов на страницу") Integer itemPerPage) {
        return ResponseEntity.ok(postCommentService.getCommentByIdPost(id, offset, itemPerPage));
    }

    @Override
    @Operation(summary = "Создание комментария к публикации")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное создание комментария",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostCommentRs.class))
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
    public ResponseEntity<PostCommentRs> addCommentByIdPost(
            @Parameter(description = "ID публикации") Integer id,
            @Parameter(schema = @Schema(implementation = CommentAddRq.class)) CommentAddRq request) {
        return ResponseEntity.ok(postCommentService.addCommentByIdPost(id, request));
    }

    @Override
    @Operation(summary = "Редактирование комментария к публикации")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное редактирование комментария",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostCommentRs.class))
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
    public ResponseEntity<PostCommentRs> putCommentByIdPost(
            @Parameter(description = "ID публикации") Integer id,
            @Parameter(description = "ID комментария публикации") Integer commentId,
            @Parameter(schema = @Schema(implementation = CommentAddRq.class)) CommentAddRq request) throws ContextedException {
        return ResponseEntity.ok(postCommentService.putCommentByIdPost(id, commentId, request));
    }

    @Override
    @Operation(summary = "Удаление комментария к публикации")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное удаление комментария",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostCommentRs.class))
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
    public ResponseEntity<PostCommentRs> deleteCommentByIdPost(
            @Parameter(description = "ID публикации") Integer id,
            @Parameter(description = "ID комментария публикации") Integer commentId) {
        return ResponseEntity.ok(postCommentService.
                deleteOrRecoveryCommentByIdPost(id, commentId, DELETE_COMMENT));
    }

    @Override
    @Operation(summary = "Восстановление комментария")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное восстановление комментария",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostCommentRs.class))
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
    public ResponseEntity<PostCommentRs> recoveryCommentByIdPost(
            @Parameter(description = "ID публикации") Integer id,
            @Parameter(description = "ID комментария публикации") Integer commentId) {
        return ResponseEntity.ok(postCommentService.
                deleteOrRecoveryCommentByIdPost(id, commentId, RECOVERY_COMMENT));
    }

    @Override
    @Operation(summary = "Подать жалобу на комментарий к публикации")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное создание жалобы на комментарий",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentComplaintRs.class))
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
    public ResponseEntity<CommentComplaintRs> addCommentComplaint(
            @Parameter(description = "ID публикации") Integer id,
            @Parameter(description = "ID комментария публикации") Integer commentId) {
        return ResponseEntity.ok(postCommentService.addNewCommentComplaint(id, commentId));
    }
}
