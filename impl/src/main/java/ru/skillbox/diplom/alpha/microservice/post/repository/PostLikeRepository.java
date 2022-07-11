package ru.skillbox.diplom.alpha.microservice.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.alpha.microservice.post.model.PostLike;

import java.util.List;
import java.util.Optional;

/**
 * PostLikeRepository
 *
 * * @author Olga Samoylova
 */

@Repository
public interface PostLikeRepository extends JpaSpecificationExecutor<PostLike>,
        PagingAndSortingRepository<PostLike, Integer> {
}
