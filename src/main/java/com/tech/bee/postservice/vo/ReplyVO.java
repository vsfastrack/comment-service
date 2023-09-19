package com.tech.bee.postservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReplyVO {
    private String content;
    private String replyId;
    private String replyIdentifier;
    private String createdBy;
    private LocalDateTime createdWhen;
    private LocalDateTime lastModifiedWhen;
}
