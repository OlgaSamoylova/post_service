package ru.skillbox.diplom.alpha.microservice.post.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diplom.alpha.library.core.dto.FileType;
import ru.skillbox.diplom.alpha.microservice.account.dto.response.FileUploadRs;
import ru.skillbox.diplom.alpha.microservice.post.response.ImageRs;
import ru.skillbox.diplom.alpha.microservice.post.service.StoragePostService;


/**
 * StoragePostResourceImpl
 *
 * @author Olga Samoylova
 */

@RestController
@RequiredArgsConstructor
@Tag(name = "Storage Service", description = "Работа с хранилищем файлов")
public class StoragePostResourceImpl implements StoragePostResource{

    private final StoragePostService storagePostService;


    @Override
    @Operation(summary = "Загрузка изображения в хранилище")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Успешная загрузка (возвращаются поля timestamp и data)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FileUploadRs.class))
                    }),
            @ApiResponse(responseCode = "401",
                    description = "Пользователь не авторизован (возвращаются поля error и error_description)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FileUploadRs.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = {@Content(mediaType = "application/json")
                    })
    })
    public ResponseEntity<ImageRs> uploadFile(FileType type, MultipartFile file) {
        return storagePostService.uploadFile(type, file);
    }
}

