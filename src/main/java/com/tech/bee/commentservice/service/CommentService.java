package com.tech.bee.commentservice.service;

import com.tech.bee.commentservice.common.ErrorDTO;
import com.tech.bee.commentservice.constants.ApiConstants;
import com.tech.bee.commentservice.dto.CommentDTO;
import com.tech.bee.commentservice.entity.CommentEntity;
import com.tech.bee.commentservice.exception.BaseCustomException;
import com.tech.bee.commentservice.mapper.CommentMapper;
import com.tech.bee.commentservice.mapper.ReplyMapper;
import com.tech.bee.commentservice.repository.CommentRepository;
import com.tech.bee.commentservice.util.AppUtil;
import com.tech.bee.commentservice.validator.CommentValidator;
import com.tech.bee.commentservice.vo.CommentVO;
import com.tech.bee.commentservice.vo.ReplyVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentValidator commentValidator;
    private final CommentMapper commentMapper;
    private final ReplyMapper replyMapper;
    private final CommentRepository commentRepository;
    private final SecurityService securityService;

    public String create(CommentDTO commentDTO){
        List<ErrorDTO> validationErrors = commentValidator.validateCreateRequest(commentDTO);
        if(CollectionUtils.isNotEmpty(validationErrors))
            throw BaseCustomException.builder().errors(validationErrors).httpStatus(HttpStatus.BAD_REQUEST).build();
        CommentEntity commentEntity = commentMapper.toEntity(commentDTO);
        commentEntity.setCreatedBy(securityService.getCurrentLoggedInUser());
        commentRepository.save(commentEntity);
        return commentEntity.getIdentifier();
    }

    public CommentVO getCommentDetails(String commentIdentifier){
        CommentEntity comment = commentRepository.findByIdentifier(commentIdentifier).orElseThrow(() -> BaseCustomException.builder().
                errors(Collections.singletonList(AppUtil.buildResourceNotFoundError(ApiConstants.KeyConstants.KEY_COMMENT))).httpStatus(HttpStatus.NOT_FOUND)
                .build());
        List<ReplyVO> replies = comment.getReplies().stream()
                                .map(replyMapper::toVO)
                                .collect(Collectors.toList());
        CommentVO commentVO = commentMapper.toVO(comment);
        commentVO.setReplies(replies);
        return commentVO;
    }

    public List<CommentVO> getCommentsByPostIdentifier(String postIdentifier){
        List<CommentEntity> comments = commentRepository.findAllByPostIdOrderByCreatedWhenDesc(postIdentifier).orElseThrow(() -> BaseCustomException.builder().
                errors(Collections.singletonList(AppUtil.buildResourceNotFoundError(ApiConstants.KeyConstants.KEY_COMMENT))).httpStatus(HttpStatus.NOT_FOUND)
                .build());
        return comments.stream().map(this::mapToVO).collect(Collectors.toList());
    }

    @Transactional
    public void modify(String commentIdentifier , CommentDTO commentDTO){
        CommentEntity comment = commentRepository.findByIdentifier(commentIdentifier).orElseThrow(() -> BaseCustomException.builder().
                errors(Collections.singletonList(AppUtil.buildResourceNotFoundError(ApiConstants.KeyConstants.KEY_COMMENT))).httpStatus(HttpStatus.NOT_FOUND)
                .build());
        List<ErrorDTO> validationErrors = commentValidator.validateOperationUser(comment);
        if(CollectionUtils.isNotEmpty(validationErrors))
            throw BaseCustomException.builder().errors(validationErrors).httpStatus(HttpStatus.FORBIDDEN).build();
        AppUtil.mergeObjectsWithProperties(commentDTO , comment , Arrays.asList("content"));
    }

    @Transactional
    public void delete(String commentIdentifier){
        CommentEntity comment = commentRepository.findByIdentifier(commentIdentifier).orElseThrow(() -> BaseCustomException.builder().
                errors(Collections.singletonList(AppUtil.buildResourceNotFoundError(ApiConstants.KeyConstants.KEY_COMMENT))).httpStatus(HttpStatus.NOT_FOUND)
                .build());
        List<ErrorDTO> validationErrors = commentValidator.validateOperationUser(comment);
        if(CollectionUtils.isNotEmpty(validationErrors))
            throw BaseCustomException.builder().errors(validationErrors).httpStatus(HttpStatus.FORBIDDEN).build();
        commentRepository.delete(comment);
    }

    private CommentVO mapToVO(CommentEntity commentEntity){
        List<ReplyVO> replies = commentEntity.getReplies().stream()
                .map(replyMapper::toVO)
                .collect(Collectors.toList());
        CommentVO commentVO = commentMapper.toVO(commentEntity);
        commentVO.setReplies(replies);
        return commentVO;
    }

}
