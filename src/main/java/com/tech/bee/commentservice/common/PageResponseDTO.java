package com.tech.bee.commentservice.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageResponseDTO <P> {
    private Object results;
    private Long totalResults;
}
