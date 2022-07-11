package ru.skillbox.diplom.alpha.microservice.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.skillbox.diplom.alpha.microservice.post.model.PostTag;

import java.util.*;

/**
 * PostSearchDto
 *
 * @author Ruslan Akbashev
 */

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class PostSearchDto {

    private Integer id;

    private Long timeFrom;

    private Long timeTo;

    private Integer authorId;

    private List<Integer> authorListId;

    private String title;

    private String postText;

    private List<Integer> postListId; //для поиска по тэгам

    private Boolean isBlocked;

    private Boolean isDeleted = false;
}
