package ru.skillbox.diplom.alpha.microservice.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.skillbox.diplom.alpha.library.core.dto.AccountDto;

/**
 * PostCommentSearchDto
 *
 * @author Ruslan Akbashev
 */

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentSearchDto {

    private Integer id;

    private Long time;

    private Integer parentId;

    private String commentText;

    private Integer postId;

    private AccountDto author;

    private Boolean isDeleted = false;

    private Boolean isBlocked = false;
}
