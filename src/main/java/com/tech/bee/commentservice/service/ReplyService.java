package com.tech.bee.commentservice.service;

import com.tech.bee.commentservice.common.ErrorDTO;
import com.tech.bee.commentservice.constants.ApiConstants;
import com.tech.bee.commentservice.dto.ReplyDTO;
import com.tech.bee.commentservice.entity.CommentEntity;
import com.tech.bee.commentservice.entity.ReplyEntity;
import com.tech.bee.commentservice.enums.Enums;
import com.tech.bee.commentservice.exception.BaseCustomException;
import com.tech.bee.commentservice.mapper.ReplyMapper;
import com.tech.bee.commentservice.repository.CommentRepository;
import com.tech.bee.commentservice.repository.ReplyRepository;
import com.tech.bee.commentservice.util.AppUtil;
import com.tech.bee.commentservice.validator.ReplyValidator;
import com.tech.bee.commentservice.vo.ReplyVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReplyService {

    private final ReplyValidator replyValidator;
    private final ReplyMapper replyMapper;
    private final ReplyRepository replyRepository;
    private final CommentRepository commentRepository;

    public String create(ReplyDTO replyDTO){
        List<ErrorDTO> validationErrors = replyValidator.validate(replyDTO);
        if(CollectionUtils.isNotEmpty(validationErrors))
            throw BaseCustomException.builder().errors(validationErrors).httpStatus(HttpStatus.BAD_REQUEST).build();
        ReplyEntity replyEntity = replyMapper.toEntity(replyDTO);
        attachToParent(replyEntity , replyDTO);
        replyRepository.save(replyEntity);
        return replyEntity.getIdentifier();
    }

    public ReplyVO getReplyDetails(String replyIdentifier){
        ReplyEntity reply = replyRepository.findByIdentifier(replyIdentifier).orElseThrow(() -> BaseCustomException.builder().
                errors(Collections.singletonList(AppUtil.buildResourceNotFoundError(ApiConstants.KeyConstants.KEY_REPLY)))
                .httpStatus(HttpStatus.NOT_FOUND)
                .build());
        ReplyVO replyVO = replyMapper.toVO(reply);
        List<ReplyVO> replyVOList = reply.getReplies().stream().map(replyMapper::toVO).collect(Collectors.toList());
        replyVO.setChildReplies(replyVOList);
        return replyVO;
    }

    public List<ReplyVO> getAllChildReplies(String replyIdentifier){
        ReplyEntity existingReply = replyRepository.findByIdentifier(replyIdentifier).orElseThrow(() -> BaseCustomException.builder().
                errors(Collections.singletonList(AppUtil.buildResourceNotFoundError(ApiConstants.KeyConstants.KEY_REPLY)))
                .httpStatus(HttpStatus.NOT_FOUND)
                .build());
        return existingReply.getReplies().stream().map(replyMapper::toVO).collect(Collectors.toList());
    }

    @Transactional
    public void modify(String replyIdentifier , ReplyDTO replyDTO){
        ReplyEntity existingReply = replyRepository.findByIdentifier(replyIdentifier).orElseThrow(() -> BaseCustomException.builder().
                errors(Collections.singletonList(AppUtil.buildResourceNotFoundError(ApiConstants.KeyConstants.KEY_REPLY))).httpStatus(HttpStatus.NOT_FOUND)
                .build());
        List<ErrorDTO> validationErrors = replyValidator.validateOperationUser(existingReply);
        if(CollectionUtils.isNotEmpty(validationErrors))
            throw BaseCustomException.builder().errors(validationErrors).httpStatus(HttpStatus.FORBIDDEN).build();
        AppUtil.mergeObjectsWithProperties(replyDTO , existingReply , Arrays.asList("content"));
    }

    @Transactional
    public void delete(String replyIdentifier){
        ReplyEntity existingReply = replyRepository.findByIdentifier(replyIdentifier).orElseThrow(() -> BaseCustomException.builder().
                errors(Collections.singletonList(AppUtil.buildResourceNotFoundError(ApiConstants.KeyConstants.KEY_REPLY))).httpStatus(HttpStatus.NOT_FOUND)
                .build());
        List<ErrorDTO> validationErrors = replyValidator.validateOperationUser(existingReply);
        if(CollectionUtils.isNotEmpty(validationErrors))
            throw BaseCustomException.builder().errors(validationErrors).httpStatus(HttpStatus.FORBIDDEN).build();
        existingReply.getReplies().clear();
        replyRepository.delete(existingReply);
    }

    private void attachToParent(ReplyEntity replyEntity , ReplyDTO replyDTO){
        if(replyDTO.getParent() == Enums.ReplyParent.COMMENT){
            CommentEntity parentComment = commentRepository.findByIdentifier(replyDTO.getParentId()).get();
            replyEntity.setParentComment(parentComment);
        }else if(replyDTO.getParent() == Enums.ReplyParent.REPLY){
            ReplyEntity parentReply = replyRepository.findByIdentifier(replyDTO.getParentId()).get();
            replyEntity.setParentReply(parentReply);
        }
    }
}
