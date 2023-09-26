package com.tech.bee.commentservice.mapper;

import com.tech.bee.commentservice.dto.CommentDTO;
import com.tech.bee.commentservice.entity.CommentEntity;
import com.tech.bee.commentservice.util.AppUtil;
import com.tech.bee.commentservice.vo.CommentVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
    @Mapping(source = "content" , target = "content")
    @Mapping(source = "postId" , target = "postId")
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
