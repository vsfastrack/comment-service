package com.tech.bee.postservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentVO {
    private String content;
    private String postId;
    private String commentId;
    private String commentIdentifier;
    private List<ReplyVO> replies;
    private String createdBy;
    private LocalDateTime createdWhen;
    private LocalDateTime lastModifiedWhen;
}
