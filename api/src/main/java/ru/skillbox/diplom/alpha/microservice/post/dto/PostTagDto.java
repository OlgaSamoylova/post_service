package ru.skillbox.diplom.alpha.microservice.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * PostTagDto
 *
 * @author OlgaSamoylova
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PostTagDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tag;
//
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private List<PostDto> postDtoList = new ArrayList<>();

}
