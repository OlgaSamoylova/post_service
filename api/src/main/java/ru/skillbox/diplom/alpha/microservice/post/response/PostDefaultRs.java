package ru.skillbox.diplom.alpha.microservice.post.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * PostAddRs
 *
 * @author Ruslan Akbashev
 */

@Data
@Schema(description = "Ответ по удалению публикации")
public class PostDefaultRs {

    @Schema(description = "Метка времени", example = "16442341250")
    private Long timestamp;

    @Schema(description = "Статус удаления публикации", example = "Ok")
    private List<MessageRs> data = new ArrayList<>();

    public void setData(MessageRs messageRs) {
        data.add(messageRs);
    }
}
