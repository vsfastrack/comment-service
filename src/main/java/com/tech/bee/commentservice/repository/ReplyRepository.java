package com.tech.bee.commentservice.repository;

import com.tech.bee.commentservice.entity.ReplyEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReplyRepository extends PagingAndSortingRepository<ReplyEntity, Long>, JpaSpecificationExecutor<ReplyEntity> {
    Optional<ReplyEntity> findByIdentifier(String replyIdentifier);
}
