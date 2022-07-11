package ru.skillbox.diplom.alpha.microservice.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.alpha.library.core.dto.AccountByIdDto;
import ru.skillbox.diplom.alpha.microservice.post.model.PostType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * baseDto
 *
 * @author Ruslan Akbashev
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Integer id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long time;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AccountByIdDto author;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String title;

    @JsonProperty(value = "post_text")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String postText;

    @JsonProperty(value = "is_blocked")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isBlocked;

    @JsonProperty(value = "comments")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<PostCommentDto> postCommentList = new ArrayList<>();

    @JsonProperty(value = "tags")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<PostTagDto> tags = new HashSet<PostTagDto>();

    @JsonProperty(value = "likes")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer likeAmount;

    @JsonProperty(value = "my_like")
    private Boolean myLike = false;

    @JsonProperty(value = "photo_url")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String imagePath;

    private PostType type;
}
