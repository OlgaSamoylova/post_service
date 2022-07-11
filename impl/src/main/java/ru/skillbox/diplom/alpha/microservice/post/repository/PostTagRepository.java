package ru.skillbox.diplom.alpha.microservice.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.alpha.microservice.post.model.PostTag;

import java.util.Optional;

/**
 * PostTagRepository
 *
 * * @author Olga Samoylova
 */

@Repository
public interface PostTagRepository extends JpaSpecificationExecutor<PostTag>,
        PagingAndSortingRepository<PostTag, Integer> {

}
