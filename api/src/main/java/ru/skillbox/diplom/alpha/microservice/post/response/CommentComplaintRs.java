package ru.skillbox.diplom.alpha.microservice.post.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * CommentComplaintRs
 *
 * @author Olga Samoylova
 */

@Data
public class CommentComplaintRs {

    @Schema(description = "Метка времени", example = "1644234125000")
    private Long timestamp;

    @JsonProperty(value = "error_description")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorDescription;

    private List<ComplaintRs> data = new ArrayList<>();

    public void addPostCommentComplaint(ComplaintRs complaintRs){
        data.add(complaintRs);
    }
}
