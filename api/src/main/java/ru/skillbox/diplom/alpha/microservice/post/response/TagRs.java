package ru.skillbox.diplom.alpha.microservice.post.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * TagRs
 *
 * @author Olga Samoylova
 */

@Data
public class TagRs {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
}
