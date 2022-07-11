package ru.skillbox.diplom.alpha.microservice.post.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skillbox.diplom.alpha.microservice.post.dto.PostDto;

import java.util.ArrayList;
import java.util.List;


/**
 * PostRs
 *
 * @author Ruslan Akbashev
 */

@Data
@Schema(description = "Ответ на получение списка публикаций/Получение публикации по ID/" +
        "Редактирование публикации/Удаление публикации/Восстановление публикации по ID")
public class PostRs {

    @Schema(description = "Метка времени", example = "16442341250")
    private Long timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Номер страницы", example = "1")
    private Integer page;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Количество элементов на странице", example = "20", defaultValue = "20")
    private Integer size;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Общее количество постов", example = "1")
    private Integer total;

    private List<PostDto> data = new ArrayList<>();

    public void addPostDto(PostDto postDto) {
        data.add(postDto);
    }
}
