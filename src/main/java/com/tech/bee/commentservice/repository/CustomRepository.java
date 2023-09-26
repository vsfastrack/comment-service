package com.tech.bee.commentservice.repository;

import com.tech.bee.commentservice.entity.ReplyEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class CustomRepository {

    private final ReplyRepository replyRepository;
    private static Specification<ReplyEntity> findChildRepliesWithIdentifier(String replyIdentifier) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.isNotEmpty(replyIdentifier))
                predicates.add(criteriaBuilder.equal(root.get("identifier"), StringUtils.trimToEmpty(replyIdentifier)));
            Predicate[] predicateList = predicates.toArray(new Predicate[0]);
            return criteriaBuilder.and(predicateList);
        };
    }
    public Page<ReplyEntity> findChildReplies(String replyIdentifier , Pageable pageable){
        Specification<ReplyEntity> specification = findChildRepliesWithIdentifier(replyIdentifier);
        return replyRepository.findAll(specification , pageable);
    }
}
