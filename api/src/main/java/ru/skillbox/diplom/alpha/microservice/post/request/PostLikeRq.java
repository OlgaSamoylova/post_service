package ru.skillbox.diplom.alpha.microservice.post.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.alpha.microservice.post.model.LikeType;

/**
 * PostLikeRq
 *
 * @author Olga Samoylova
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на установку \"Лайка\"")
public class PostLikeRq {

    @JsonProperty(value = "author_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer authorId;

    @JsonProperty(value = "item_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "ID объекта у которого необходимо получить \"Лайки\"", example = "number")
    private Integer itemId;

    @JsonProperty(value = "post_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer post_id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Available values : POST, COMMENT")
    private LikeType type;
}
