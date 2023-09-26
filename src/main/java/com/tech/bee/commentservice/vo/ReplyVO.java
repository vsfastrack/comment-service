package com.tech.bee.commentservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReplyVO {
    private String content;
    private String replyId;
    private String identifier;
    private List<ReplyVO> childReplies;
    private String createdBy;
    private LocalDateTime createdWhen;
    private LocalDateTime lastModifiedWhen;
}
