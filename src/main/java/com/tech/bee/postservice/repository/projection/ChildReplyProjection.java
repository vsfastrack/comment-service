package com.tech.bee.postservice.repository.projection;

import com.tech.bee.postservice.entity.ReplyEntity;

import java.util.List;

public interface ChildReplyProjection {
    List<ReplyEntity> replies();
}
