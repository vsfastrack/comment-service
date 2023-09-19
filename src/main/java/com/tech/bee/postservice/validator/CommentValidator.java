package com.tech.bee.postservice.validator;

import com.tech.bee.postservice.common.ErrorDTO;
import com.tech.bee.postservice.constants.ApiConstants;
import com.tech.bee.postservice.dto.CommentDTO;
import com.tech.bee.postservice.enums.Enums;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentValidator {

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

    private void validatePost(CommentDTO commentDTO , List<ErrorDTO> notificationErrors){
        //TODO use webClient to communicate with postService and check if its a valid postId and user being passed
    }
}
