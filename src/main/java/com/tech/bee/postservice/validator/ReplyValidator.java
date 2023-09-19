package com.tech.bee.postservice.validator;

import com.tech.bee.postservice.common.ErrorDTO;
import com.tech.bee.postservice.constants.ApiConstants;
import com.tech.bee.postservice.dto.ReplyDTO;
import com.tech.bee.postservice.entity.CommentEntity;
import com.tech.bee.postservice.entity.ReplyEntity;
import com.tech.bee.postservice.enums.Enums;
import com.tech.bee.postservice.repository.CommentRepository;
import com.tech.bee.postservice.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ReplyValidator {

    private final ReplyRepository replyRepository;
    private final CommentRepository commentRepository;

    public List<ErrorDTO> validate(ReplyDTO replyDTO){
        List<ErrorDTO> notificationErrors = new ArrayList<>();
        validateContent(replyDTO ,notificationErrors);
        validateParentExistence(replyDTO , notificationErrors);
        return notificationErrors;
    }

    public void validateContent(ReplyDTO replyDTO , List<ErrorDTO> notificationErrors){
        if(StringUtils.isEmpty(replyDTO.getContent()))
            notificationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_FIELD_CANNOT_BE_EMPTY)
                    .message(ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_CANNOT_BE_EMPTY)
                    .key(ApiConstants.KeyConstants.KEY_REPLY)
                    .category(Enums.ErrorCategory.VALIDATION_ERROR).build());
        validateLength(replyDTO , notificationErrors);
    }

    private void validateLength(ReplyDTO replyDTO , List<ErrorDTO> notificationErrors){
        if(StringUtils.isNotEmpty(replyDTO.getContent()) && StringUtils.length(replyDTO.getContent()) > 1000)
            notificationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_FIELD_VALUE_LENGTH_GREATER_THAN_DEFINED_LENGTH)
                    .message(String.format(ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_VALUE_LENGTH_GREATER_THAN_DEFINED_LENGTH, "1000"))
                    .key(ApiConstants.KeyConstants.KEY_REPLY)
                    .category(Enums.ErrorCategory.VALIDATION_ERROR).build());
    }

    private void validateParentExistence(ReplyDTO replyDTO , List<ErrorDTO> notificationErrors){
        if(Enums.ReplyParent.REPLY.equals(replyDTO.getParent())){
            Optional<ReplyEntity> existingReply = replyRepository.findByIdentifier(replyDTO.getParentId());
            if(!existingReply.isPresent()){
                notificationErrors.add(ErrorDTO.builder()
                        .code(ApiConstants.ErrorCodeConstants.CODE_RESOURCE_NOT_FOUND)
                        .message(ApiConstants.ErrorMsgConstants.MESSAGE_RESOURCE_NOT_FOUND)
                        .key(ApiConstants.KeyConstants.KEY_REPLY)
                        .category(Enums.ErrorCategory.BUSINESS_VALIDATION_ERROR).build());
            }
        }else if(Enums.ReplyParent.COMMENT.equals(replyDTO.getParent())) {
            Optional<CommentEntity> existingComment = commentRepository.findByIdentifier(replyDTO.getParentId());
            if (!existingComment.isPresent()) {
                notificationErrors.add(ErrorDTO.builder()
                        .code(ApiConstants.ErrorCodeConstants.CODE_RESOURCE_NOT_FOUND)
                        .message(ApiConstants.ErrorMsgConstants.MESSAGE_RESOURCE_NOT_FOUND)
                        .key(ApiConstants.KeyConstants.KEY_COMMENT)
                        .category(Enums.ErrorCategory.BUSINESS_VALIDATION_ERROR).build());
            }
        }
    }

}
