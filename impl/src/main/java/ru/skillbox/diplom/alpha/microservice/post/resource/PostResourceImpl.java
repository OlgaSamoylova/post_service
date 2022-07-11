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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.alpha.microservice.post.model.PostTag;
import ru.skillbox.diplom.alpha.microservice.post.request.PostAddRq;
import ru.skillbox.diplom.alpha.microservice.post.response.*;
import ru.skillbox.diplom.alpha.microservice.post.service.PostService;

import java.util.List;

/**
 * PostResourceImpl
 *
 * @author Ruslan Akbashev, Olga Samoylova
 */
@Setter
@Getter
@RestController
@RequiredArgsConstructor
@Tag(name = "posts", description = "Работа с публикациями")
public class PostResourceImpl implements PostResource {

    private final PostService postService;

    @Override
    @Operation(summary = "Поиск публикации")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение публикации",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostRs.class))
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
    public ResponseEntity<PostRs> getPostAll(@Parameter(description = "Текст публикации") String text,
                                             @Parameter(description = "Дата публикации ОТ") Long dateFrom,
                                             @Parameter(description = "Дата публикации ДО") Long dateTo,
                                             @Parameter(description = "Имя автора для поиска") String author,
                                             @Parameter(description = "Тэги поста") List<String> tags,
                                             @Parameter(description = "Настройка пагинации") Pageable pageable) {
        return ResponseEntity.ok(postService.getPostAll(text,
                dateFrom,
                dateTo,
                author,
                tags,
                pageable));
    }


    @Override
    @Operation(summary = "Получение публикации по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение публикации",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostRs.class))
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
    public ResponseEntity<PostRs> getPostById(@Parameter(description = "ID публикации") Integer id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @Override
    @Operation(summary = "Редактирование публикации")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное редактирование публикации",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostRs.class))
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
    public ResponseEntity<PostRs> putPostById(@Parameter(description = "ID публикации") Integer id,
                                              @Parameter(description = "Отложить до даты определенной даты")
                                              Long publishDate,
                                              @Parameter(schema = @Schema(implementation = PostAddRq.class))
                                                          PostAddRq request) throws Exception {
        return ResponseEntity.ok(postService.putPostById(id, publishDate, request));
    }

    @Override
    @Operation(summary = "Удаление публикации")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное удаление публикации",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostRs.class))
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
    public ResponseEntity<PostDefaultRs> deletePostById(
            @Parameter(description = "ID публикации") Integer id) throws ContextedException {
        return ResponseEntity.ok(postService.deletePostById(id));
    }

    @Override
    @Operation(summary = "Восстановление публикации по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное восстановление публикации",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostRs.class))
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
    public ResponseEntity<PostRs> recoveryPostById(
            @Parameter(description = "ID публикации") Integer id) throws ContextedException {
        return ResponseEntity.ok(postService.recoveryPostById(id));
    }

    @Override
    @Operation(summary = "Создание новой публикации")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное создание публикации", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostAddRs.class))
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
    public ResponseEntity<PostDefaultRs> addNewPost(
            @Parameter(description = "Отложить до даты определенной даты") long publishDate,
            @Parameter(schema = @Schema(implementation = PostAddRq.class)) PostAddRq request) throws Exception {
        return ResponseEntity.ok(postService.addNewPost(publishDate, request));
    }

    @Override
    @Operation(summary = "Подать жалобу на публикацию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное создание жалобы на публикацию",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostComplaintRs.class))
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
    public ResponseEntity<PostComplaintRs> addNewComplaint(
            @Parameter(description = "ID публикации") Integer id) {
        return ResponseEntity.ok(postService.addNewComplaint(id));
    }

    @Override
    @Operation(summary = "Получение списка новостей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка новостей",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostRs.class))
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
    public ResponseEntity<PostRs> getFeeds(Pageable pageable)  {
        return ResponseEntity.ok(postService.getFeeds(pageable));
    }

    @Override
    @Operation(summary = "Получение списка постов пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Успешное получение списка постов пользователя",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostRs.class))
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
    public ResponseEntity<PostRs> getPostWall() {
        return ResponseEntity.ok(postService.getPostWall());
    }

    @Override
    @Operation(summary = "Получение списка постов пользователя по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Успешное получение списка постов пользователя по id",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostRs.class))
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
    public ResponseEntity<PostRs> getPostWallById(@Parameter(description = "ID пользователя") Integer userId) {
        return ResponseEntity.ok(postService.getPostWallById(userId));
    }
}
