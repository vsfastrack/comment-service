package com.tech.bee.postservice.service;

import com.tech.bee.postservice.common.ErrorDTO;
import com.tech.bee.postservice.constants.ApiConstants;
import com.tech.bee.postservice.dto.ReplyDTO;
import com.tech.bee.postservice.entity.ReplyEntity;
import com.tech.bee.postservice.exception.BaseCustomException;
import com.tech.bee.postservice.mapper.ReplyMapper;
import com.tech.bee.postservice.repository.ReplyRepository;
import com.tech.bee.postservice.util.AppUtil;
import com.tech.bee.postservice.validator.ReplyValidator;
import com.tech.bee.postservice.vo.ReplyVO;
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

    public String create(ReplyDTO replyDTO){
        List<ErrorDTO> validationErrors = replyValidator.validate(replyDTO);
        if(CollectionUtils.isNotEmpty(validationErrors))
            throw BaseCustomException.builder().errors(validationErrors).httpStatus(HttpStatus.BAD_REQUEST).build();
        ReplyEntity replyEntity = replyMapper.toEntity(replyDTO);
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
        AppUtil.mergeObjectsWithProperties(replyDTO , existingReply , Arrays.asList("content"));
    }

    @Transactional
    public void delete(String replyIdentifier){
        ReplyEntity existingReply = replyRepository.findByIdentifier(replyIdentifier).orElseThrow(() -> BaseCustomException.builder().
                errors(Collections.singletonList(AppUtil.buildResourceNotFoundError(ApiConstants.KeyConstants.KEY_REPLY))).httpStatus(HttpStatus.NOT_FOUND)
                .build());
        existingReply.getReplies().clear();
        replyRepository.delete(existingReply);
    }
}
