package ru.skillbox.diplom.alpha.microservice.post.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * PostComplaintRs
 *
 * @author Olga Samoylova
 */

@Data
@Schema(description = "Ответ на результат жалобы на публикацию")
public class PostComplaintRs {

    @Schema(description = "Метка времени", example = "16442341250")
    private Long timestamp;

    @JsonProperty(value = "error_description")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Описание ошибки", example = "items: OrderedMap { \"type\": " +
            "\"object\", \"properties\": OrderedMap { \"error_description\": OrderedMap {" +
            " \"type\": \"string\", \"description\": \"Error description\", \"enum\": List " +
            "[ \"Unauthorized\", \"An authorization code must be supplied\", \"Redirect URI mismatch\"," +
            " \"Invalid authorization code: CODE\", \"Bad credentials\" ] } }, " +
            "\"$$ref\": \"#/components/schemas/ErrorDescription")
    private String errorDescription;

    private List<ComplaintRs> data = new ArrayList();

    public void addPostComplaint(ComplaintRs complaintRs){
        data.add(complaintRs);
    }

}
