package com.tech.bee.commentservice.validator;

import com.tech.bee.commentservice.common.ErrorDTO;
import com.tech.bee.commentservice.constants.ApiConstants;
import com.tech.bee.commentservice.dto.ReplyDTO;
import com.tech.bee.commentservice.entity.CommentEntity;
import com.tech.bee.commentservice.entity.ReplyEntity;
import com.tech.bee.commentservice.enums.Enums;
import com.tech.bee.commentservice.repository.CommentRepository;
import com.tech.bee.commentservice.repository.ReplyRepository;
import com.tech.bee.commentservice.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
@Slf4j
public class ReplyValidator {

    private final ReplyRepository replyRepository;
    private final CommentRepository commentRepository;
    private final SecurityService securityService;

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
                log.error("Existing reply found with id");
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
    public List<ErrorDTO> validateOperationUser(ReplyEntity replyEntity){
        List<ErrorDTO> validationErrors = new ArrayList<>();
        String currentLoggedInUser = securityService.getCurrentLoggedInUser();
        if(!currentLoggedInUser.equals(replyEntity.getCreatedBy()))
            validationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_OPERATION_FORBIDDEN)
                    .message(ApiConstants.ErrorMsgConstants.MESSAGE_OPERATION_FORBIDDEN)
                    .key(ApiConstants.KeyConstants.KEY_USER)
                    .category(Enums.ErrorCategory.BUSINESS_VALIDATION_ERROR).build());
        return validationErrors;
    }

}
