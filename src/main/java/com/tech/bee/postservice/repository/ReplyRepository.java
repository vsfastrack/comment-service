package com.tech.bee.postservice.repository;

import com.tech.bee.postservice.entity.ReplyEntity;
import com.tech.bee.postservice.repository.projection.ChildReplyProjection;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReplyRepository extends PagingAndSortingRepository<ReplyEntity, Long>, JpaSpecificationExecutor<ReplyEntity> {
    Optional<ReplyEntity> findByIdentifier(String replyIdentifier);
}
