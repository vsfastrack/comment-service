package com.tech.bee.commentservice.repository.projection;

import com.tech.bee.commentservice.entity.ReplyEntity;

import java.util.List;

public interface ChildReplyProjection {
    List<ReplyEntity> replies();
}
