package ru.skillbox.diplom.alpha.microservice.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.alpha.library.core.dto.AccountByIdDto;
import ru.skillbox.diplom.alpha.library.core.dto.AccountDto;

import java.util.ArrayList;
import java.util.List;

/**
 * PostCommentDto
 *
 * @author Ruslan Akbashev
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostCommentDto {
    private Integer id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long time;

    @JsonProperty(value = "parent_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer parentId;

    @JsonProperty(value = "comment_text")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String commentText;

    @JsonProperty(value = "post_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer postId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AccountByIdDto author;

    @JsonProperty(value = "is_blocked")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isBlocked;

    @JsonProperty(value = "likes")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer likeAmount;

    @JsonProperty(value = "my_like")
    private Boolean myLike = false;

    @JsonProperty(value = "sub_comments")
    private List<PostCommentDto> subComment = new ArrayList<>();
}
