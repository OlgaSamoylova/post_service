package ru.skillbox.diplom.alpha.microservice.post.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ru.skillbox.diplom.alpha.microservice.post.dto.PostDto;
import ru.skillbox.diplom.alpha.microservice.post.dto.PostTagDto;

import java.util.ArrayList;
import java.util.List;

/**
 * PostTagRs
 *
 * @author Olga Samoylova
 */

@Data
public class PostTagRs {
    private Long timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Integer total;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Integer offset;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Integer perPage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<PostTagDto> data = new ArrayList();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<PostDto> dataPosts = new ArrayList();
}
