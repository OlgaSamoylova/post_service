package ru.skillbox.diplom.alpha.microservice.post.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * Comment
 *
 * @author Ruslan Akbashev
 */

@Getter
@Setter
@Table(name = "post_comment")
@Accessors(chain = true)
@Entity
public class PostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Long time;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "comment_text", nullable = false)
    private String commentText;

    @ManyToOne
    private Post post;

    @Column(name = "author_id")
    private Integer authorId;

    @Column(name = "is_blocked", nullable = false)
    private Boolean isBlocked;

    @Column(name = "is_delete")
    private Boolean isDelete;

    private Integer likeAmount;

    @Column(name = "my_like")
    private Boolean myLike;


}
