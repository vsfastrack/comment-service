package com.tech.bee.postservice.service;

import com.tech.bee.postservice.common.ErrorDTO;
import com.tech.bee.postservice.constants.ApiConstants;
import com.tech.bee.postservice.dto.CommentDTO;
import com.tech.bee.postservice.entity.CommentEntity;
import com.tech.bee.postservice.exception.BaseCustomException;
import com.tech.bee.postservice.mapper.CommentMapper;
import com.tech.bee.postservice.mapper.ReplyMapper;
import com.tech.bee.postservice.repository.CommentRepository;
import com.tech.bee.postservice.util.AppUtil;
import com.tech.bee.postservice.validator.CommentValidator;
import com.tech.bee.postservice.vo.CommentVO;
import com.tech.bee.postservice.vo.ReplyVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentValidator commentValidator;
    private final CommentMapper commentMapper;
    private final ReplyMapper replyMapper;
    private final CommentRepository commentRepository;

    public String create(CommentDTO commentDTO){
        List<ErrorDTO> validationErrors = commentValidator.validateCreateRequest(commentDTO);
        if(CollectionUtils.isNotEmpty(validationErrors))
            throw BaseCustomException.builder().errors(validationErrors).httpStatus(HttpStatus.BAD_REQUEST).build();
        CommentEntity commentEntity = commentMapper.toEntity(commentDTO);
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

    @Transactional
    public void modify(String commentIdentifier , CommentDTO commentDTO){
        CommentEntity comment = commentRepository.findByIdentifier(commentIdentifier).orElseThrow(() -> BaseCustomException.builder().
                errors(Collections.singletonList(AppUtil.buildResourceNotFoundError(ApiConstants.KeyConstants.KEY_COMMENT))).httpStatus(HttpStatus.NOT_FOUND)
                .build());
        AppUtil.mergeObjectsWithProperties(commentDTO , comment , Arrays.asList("content"));
    }

    @Transactional
    public void delete(String commentIdentifier){
        CommentEntity comment = commentRepository.findByIdentifier(commentIdentifier).orElseThrow(() -> BaseCustomException.builder().
                errors(Collections.singletonList(AppUtil.buildResourceNotFoundError(ApiConstants.KeyConstants.KEY_COMMENT))).httpStatus(HttpStatus.NOT_FOUND)
                .build());
        commentRepository.delete(comment);
    }

}
