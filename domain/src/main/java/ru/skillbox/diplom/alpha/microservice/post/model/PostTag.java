package ru.skillbox.diplom.alpha.microservice.post.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.*;

/**
 * PostTag
 *
 * @author Olga Samoylova
 */

@Getter
@Setter
@Table(name = "tag", schema = "post")
@Entity
@NoArgsConstructor
public class PostTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String tag;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.EAGER)//mappedBy = "tags")
    @JoinTable(name = "post2tag",
            joinColumns = {@JoinColumn(name = "tag_id")},
            inverseJoinColumns = {@JoinColumn(name = "post_id")})
    private List<Post> posts = new ArrayList<>();

    public PostTag(String value) {
        this.tag = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostTag)) return false;
        PostTag postTag = (PostTag) o;
        return Objects.equals(getId(), postTag.getId())
                && Objects.equals(getTag(), postTag.getTag())
                && Objects.equals(getPosts(), postTag.getPosts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTag(), getPosts());
    }
}
