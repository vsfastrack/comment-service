package com.tech.bee.commentservice.validator;

import com.tech.bee.commentservice.common.ErrorDTO;
import com.tech.bee.commentservice.constants.ApiConstants;
import com.tech.bee.commentservice.dto.CommentDTO;
import com.tech.bee.commentservice.entity.CommentEntity;
import com.tech.bee.commentservice.enums.Enums;
import com.tech.bee.commentservice.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentValidator {

    private final SecurityService securityService;

    public List<ErrorDTO> validateCreateRequest(CommentDTO commentDTO){
        List<ErrorDTO> notificationErrors = new ArrayList<>();
        validateContent(commentDTO , notificationErrors);
        validatePost(commentDTO , notificationErrors);
        return notificationErrors;
    }

    private void validateContent(CommentDTO commentDTO , List<ErrorDTO> notificationErrors){
        if(StringUtils.isEmpty(commentDTO.getContent()))
            notificationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_FIELD_CANNOT_BE_EMPTY)
                    .message(ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_CANNOT_BE_EMPTY)
                    .key(ApiConstants.KeyConstants.KEY_CONTENT)
                    .category(Enums.ErrorCategory.VALIDATION_ERROR).build());
        validateLength(commentDTO , notificationErrors);
    }

    private void validateLength(CommentDTO commentDTO , List<ErrorDTO> notificationErrors){
        if(StringUtils.isNotEmpty(commentDTO.getContent()) && StringUtils.length(commentDTO.getContent()) > 1000)
            notificationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_FIELD_VALUE_LENGTH_GREATER_THAN_DEFINED_LENGTH)
                    .message(String.format(ApiConstants.ErrorMsgConstants.MESSAGE_FIELD_VALUE_LENGTH_GREATER_THAN_DEFINED_LENGTH, "1000"))
                    .key(ApiConstants.KeyConstants.KEY_CONTENT)
                    .category(Enums.ErrorCategory.VALIDATION_ERROR).build());
    }

    public List<ErrorDTO> validateOperationUser(CommentEntity commentEntity){
        List<ErrorDTO> validationErrors = new ArrayList<>();
        String currentLoggedInUser = securityService.getCurrentLoggedInUser();
        if(!currentLoggedInUser.equals(commentEntity.getCreatedBy()))
            validationErrors.add(ErrorDTO.builder()
                    .code(ApiConstants.ErrorCodeConstants.CODE_OPERATION_FORBIDDEN)
                    .message(ApiConstants.ErrorMsgConstants.MESSAGE_OPERATION_FORBIDDEN)
                    .key(ApiConstants.KeyConstants.KEY_USER)
                    .category(Enums.ErrorCategory.BUSINESS_VALIDATION_ERROR).build());
        return validationErrors;
    }

    private void validatePost(CommentDTO commentDTO , List<ErrorDTO> notificationErrors){
        //TODO use webClient to communicate with postService and check if its a valid postId and user being passed
    }
}
