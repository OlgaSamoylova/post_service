package ru.skillbox.diplom.alpha.microservice.post.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * PostAddRq
 *
 * @author Ruslan Akbashev, Olga Samoylova
 */
@Getter
@Setter
@Schema(description = "Запрос на создание/редактирование публикации")
public class PostAddRq {
    @Schema(description = "Название публикации", example = "string")
    private String title;

    @JsonProperty("post_text")
    @Schema(description = "Текст публикации", example = "string")
    private String postText;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Название тэга", example = "string")
    private Set<String> tags = new HashSet<>();

    @JsonProperty("photo_url")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Фото публикации", example = "http://res.cloudinary.com/...")
    private String photoUrl;
}