package com.tech.bee.commentservice.enums;

import lombok.Getter;

public class Enums {
    @Getter
    public enum ErrorStatus {
        BAD_REQUEST ,INTERNAL_SERVER_ERROR,UNAUTHENTICATED,UNAUTHORISED,
    }
    @Getter
    public enum ErrorCategory{
        VALIDATION_ERROR,BUSINESS_VALIDATION_ERROR
    }
    @Getter
    public enum SortDirection{
        ASC,DESC
    }
    @Getter
    public enum ReplyParent{
        COMMENT,REPLY
    }
}
