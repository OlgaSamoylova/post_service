package ru.skillbox.diplom.alpha.microservice.post.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * PostLike
 *
 * @author Olga Samoylova
 */

@Getter
@Setter
@Table(name = "post_like", schema = "post")
@Accessors(chain = true)
@Entity
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Long time;

    @Column(name = "author_id", nullable = false)
    private Integer authorId;

    @Column(name = "item_id", nullable = false)
    private Integer itemId;

    @Column(nullable = false, columnDefinition = "enum")
    @Enumerated(EnumType.STRING)
    private LikeType type;
}
