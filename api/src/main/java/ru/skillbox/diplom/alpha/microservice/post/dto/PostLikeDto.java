package ru.skillbox.diplom.alpha.microservice.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.alpha.library.core.dto.AccountDto;

/**
 * PostLikeDto
 *
 * @author OlgaSamoylova
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostLikeDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long time;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AccountDto person;

    @JsonProperty(value = "item_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer itemId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String type;
}
