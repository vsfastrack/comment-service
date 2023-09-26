package com.tech.bee.commentservice.dto;

import com.tech.bee.commentservice.enums.Enums;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReplyDTO {
    private String content;
    private String parentId;
    private Enums.ReplyParent parent;
}
