package ru.skillbox.diplom.alpha.microservice.post.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * ErrorPostRs
 *
 * @author Ruslan Akbashev
 */

@Data
@Schema(description = "Ответ в случае ошибки 400/401")
public class ErrorPostRs {
    @Schema(description = "Тип ошибки", example = "invalid_request")
    private String error;

    @JsonProperty(value = "error_description")
    @Schema(description = "Описание ошибки", example = "items: OrderedMap { \"type\": " +
            "\"object\", \"properties\": OrderedMap { \"error_description\": OrderedMap {" +
            " \"type\": \"string\", \"description\": \"Error description\", \"enum\": List " +
            "[ \"Unauthorized\", \"An authorization code must be supplied\", \"Redirect URI mismatch\"," +
            " \"Invalid authorization code: CODE\", \"Bad credentials\" ] } }, " +
            "\"$$ref\": \"#/components/schemas/ErrorDescription")
    private String errorDescription;
}
