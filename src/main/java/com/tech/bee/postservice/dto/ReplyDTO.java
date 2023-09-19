package com.tech.bee.postservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReplyDTO {
    private String content;
    private String parentId;
}
