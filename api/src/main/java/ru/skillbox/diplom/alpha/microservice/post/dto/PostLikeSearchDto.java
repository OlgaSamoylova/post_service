package ru.skillbox.diplom.alpha.microservice.post.dto;

import lombok.*;
import lombok.experimental.Accessors;
import ru.skillbox.diplom.alpha.microservice.post.model.LikeType;

/**
 * PostLikeSearchDto
 *
 * @author OlgaSamoylova
 */

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PostLikeSearchDto {
    private Integer authorId;
    private Integer itemId;
    private LikeType type = LikeType.POST;
}
