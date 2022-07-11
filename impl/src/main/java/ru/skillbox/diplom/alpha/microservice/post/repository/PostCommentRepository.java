package ru.skillbox.diplom.alpha.microservice.post.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.alpha.microservice.post.model.PostComment;

/**
 * PostCommentRepository
 *
 * @author Ruslan Akbashev
 */
@Repository
public interface PostCommentRepository extends JpaSpecificationExecutor<PostComment>,
        PagingAndSortingRepository<PostComment, Integer> {
}
