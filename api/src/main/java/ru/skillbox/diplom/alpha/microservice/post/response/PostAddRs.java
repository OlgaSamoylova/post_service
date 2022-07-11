package ru.skillbox.diplom.alpha.microservice.post.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * PostAddRs
 *
 * @author Ruslan Akbashev
 */

@Data
@Schema(description = "Ответ по созданию новой публикации")
public class PostAddRs {

    @Schema(description = "Статус добавление новой публикации", example = "true/false")
    private boolean status;
}
