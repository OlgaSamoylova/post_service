package ru.skillbox.diplom.alpha.microservice.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Post2TagSearchDto
 *
 * @author OlgaSamoylova
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post2TagSearchDto {
    Integer postId;
    Integer tagId;
}

