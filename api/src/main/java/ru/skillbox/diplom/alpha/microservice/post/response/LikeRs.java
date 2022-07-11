package ru.skillbox.diplom.alpha.microservice.post.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * LikeRs
 *
 * @author Olga Samoylova
 */

@Data
@Schema(description = "Ответ на получение списка публикаций/Получение публикации по ID/" +
        "Редактирование публикации/Удаление публикации/Восстановление публикации по ID")
public class LikeRs {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean liked;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer likes;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Integer> users;
}
