package ru.skillbox.diplom.alpha.microservice.post.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * CommentAddRq
 *
 * @author Ruslan Akbashev
 */
@Getter
@Setter
@Schema(description = "Запрос на создание/редактирование комментария")
public class CommentAddRq {
    @JsonProperty(value = "parent_id")
    @Schema(description = "ID родительского комментария", example = "int")
    private Integer parentId;

    @JsonProperty(value = "comment_text")
    @Schema(description = "Текст комментария", example = "string")
    private String commentText;
}
