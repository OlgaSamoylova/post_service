package ru.skillbox.diplom.alpha.microservice.post.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skillbox.diplom.alpha.microservice.post.dto.PostCommentDto;

import java.util.ArrayList;
import java.util.List;

/**
 * PostCommentRs
 *
 * @author Ruslan Akbashev
 */
@Data
public class PostCommentRs {

    @Schema(description = "Метка времени", example = "1644234125000")
    private Long timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer total;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer offset;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer perPage;

    private List<PostCommentDto> data = new ArrayList<>();

    public void addPostCommentDto(PostCommentDto postCommentDto){
        data.add(postCommentDto);
    }
}
