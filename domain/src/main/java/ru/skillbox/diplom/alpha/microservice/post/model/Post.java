package ru.skillbox.diplom.alpha.microservice.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Post
 *
 * @author Ruslan Akbashev
 */

@Getter
@Setter
@Table(name = "post")
@Entity
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Long time;

    @Column(name = "author_id", nullable = false)
    private Integer authorId;

    @Column(nullable = false)
    private String title;

    @Column(name = "post_text", nullable = false)
    private String postText;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "is_blocked", nullable = false)
    private Boolean isBlocked;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post", fetch = FetchType.EAGER)
    private List<PostComment> commentList = Collections.EMPTY_LIST;

    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(name = "post2tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<PostTag> tags = new HashSet<>();

    private Integer likeAmount;

    @Column(name = "my_like")
    private Boolean myLike;

    public Post(Long time, Integer authorId,
                String title, String postText, Boolean isBlocked,
                Boolean isDelete) {
        this.time = time;
        this.authorId = authorId;
        this.title = title;
        this.postText = postText;
        this.isBlocked = isBlocked;
        this.isDelete = isDelete;
    }

    public List<PostComment> getCommentsList() {
        this.commentList = commentList.stream().filter(c -> c.getIsDelete().equals(false))
                .collect(Collectors.toList());
        return this.commentList;
    }


    public void addNewTag(PostTag postTag) {
        tags.add(postTag);
        postTag.getPosts().add(this);
    }

    @PreRemove
    public void removeTag() {
        tags.forEach(postTag -> postTag.getPosts().remove(this));
        tags.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;
        Post post = (Post) o;
        return Objects.equals(getId(), post.getId())
                && Objects.equals(getTime(), post.getTime())
                && Objects.equals(getAuthorId(), post.getAuthorId())
                && Objects.equals(getTitle(), post.getTitle())
                && Objects.equals(getPostText(), post.getPostText())
                && Objects.equals(getIsBlocked(), post.getIsBlocked())
                && Objects.equals(getIsDelete(), post.getIsDelete())
                && Objects.equals(getCommentList(), post.getCommentList())
                && Objects.equals(getTags(), post.getTags())
                && Objects.equals(getLikeAmount(), post.getLikeAmount())
                && Objects.equals(getMyLike(), post.getMyLike());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getTime(),
                getAuthorId(),
                getTitle(),
                getPostText(),
                getIsBlocked(),
                getIsDelete(),
                getCommentList(),
                getTags(),
                getLikeAmount(),
                getMyLike());
    }
}
