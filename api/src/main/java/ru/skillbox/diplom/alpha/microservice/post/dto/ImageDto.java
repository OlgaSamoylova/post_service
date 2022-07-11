package ru.skillbox.diplom.alpha.microservice.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ImageDto
 *
 * @author Olga Samoylova
 */
@Data
@AllArgsConstructor
@Schema(description = "Dto загрузки изображения в хранилище")
public class ImageDto {

    @Schema(description = "Url изображения в хранилище")
    @JsonProperty(value = "photo_url")
    String photoUrl;

    @Schema(description = "Название изображения")
    @JsonProperty(value = "photo_name")
    String photoName;
}

