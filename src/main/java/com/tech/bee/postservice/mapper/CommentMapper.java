package com.tech.bee.postservice.mapper;

import com.tech.bee.postservice.dto.CommentDTO;
import com.tech.bee.postservice.entity.CommentEntity;
import com.tech.bee.postservice.util.AppUtil;
import com.tech.bee.postservice.vo.CommentVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
    @Mapping(source = "content" , target = "content")
    @Mapping(source = "postId" , target = "postID")
    @Mapping(ignore = true, target = "replies")
    @Mapping(ignore = true, target = "createdBy")
    @Mapping(expression = "java(toId())" , target="commentId")
    CommentEntity toEntity(CommentDTO commentDTO);

    @Mapping(source = "content" , target = "content")
    @Mapping(source = "commentId" , target = "commentId")
    @Mapping(source = "identifier" , target = "identifier")
    @Mapping(source = "createdBy" , target = "createdBy")
    @Mapping(source = "createdWhen" , target = "createdWhen")
    @Mapping(source = "lastModifiedWhen" , target = "lastModifiedWhen")
    CommentVO toVO(CommentEntity commentEntity);

    default String toId(){
        return AppUtil.generateIdentifier("CM");
    }

}
