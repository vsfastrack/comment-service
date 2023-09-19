package com.tech.bee.postservice.dto;

import com.tech.bee.postservice.enums.Enums;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReplyDTO {
    private String content;
    private String parentId;
    private Enums.ReplyParent parent;
}
