package com.tech.bee.postservice.repository;

import com.tech.bee.postservice.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<CommentEntity, Long>, JpaSpecificationExecutor<CommentEntity> {
    Optional<CommentEntity> findByIdentifier(String identifier);
}
