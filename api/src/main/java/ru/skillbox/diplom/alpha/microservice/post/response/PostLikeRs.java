package ru.skillbox.diplom.alpha.microservice.post.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * PostLikeRs
 *
 * @author Olga Samoylova
 */

@Data
@Schema(description = "Ответ на \"Был ли поставлен лайк пользователем\"/" +
        "\"Получить список пользователей оставивших лайк\"/" +
        "\"Поставить лайк\"/\"Убрать лайк\"")
public class PostLikeRs {
    @Schema(description = "Метка времени", example = "16442341250")
    private Long timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<LikeRs> data = new ArrayList();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorDescription;

    public void addLike(LikeRs likeRs) {
        data.add(likeRs);
    }
}
