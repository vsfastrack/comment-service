package com.tech.bee.postservice.constants;

public class ApiConstants {
    public static final class PathConstants{
        public static final String PATH_COMMENT_RESOURCE="/api/v1/comments";
        public static final String PATH_REPLY_RESOURCE="/api/v1/replies";
    }
    public static final class ErrorCodeConstants{
        public static final String CODE_FIELD_CANNOT_BE_EMPTY="400.001";
        public static final String CODE_FIELD_VALUE_LENGTH_GREATER_THAN_DEFINED_LENGTH="404.002";
        public static final String CODE_RESOURCE_NOT_FOUND="404.001";
        public static final String CODE_RESOURCE_CONFLICT ="409.001";
    }
    public static final class ErrorMsgConstants{
        public static final String MESSAGE_FIELD_CANNOT_BE_EMPTY="Field cannot be empty";
        public static final String MESSAGE_FIELD_VALUE_LENGTH_GREATER_THAN_DEFINED_LENGTH="Field length cannot be greater than %s characters";
        public static final String MESSAGE_RESOURCE_NOT_FOUND="Resource not found";
        public static final String MESSAGE_RESOURCE_CONFLICT="Resource already exists";
    }
    public static final class KeyConstants{
        public static final String KEY_CONTENT="content";
        public static final String KEY_COMMENT="comment";
        public static final String KEY_REPLY="reply";
    }
}
