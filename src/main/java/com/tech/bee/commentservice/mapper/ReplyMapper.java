package com.tech.bee.commentservice.mapper;

import com.tech.bee.commentservice.dto.ReplyDTO;
import com.tech.bee.commentservice.entity.ReplyEntity;
import com.tech.bee.commentservice.util.AppUtil;
import com.tech.bee.commentservice.vo.ReplyVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReplyMapper {

    @Mapping(source = "content" , target = "content")
    @Mapping(source = "replyId" , target = "replyId")
    @Mapping(source = "identifier" , target = "identifier")
    @Mapping(source = "createdBy" , target = "createdBy")
    @Mapping(source = "createdWhen" , target = "createdWhen")
    @Mapping(source = "lastModifiedWhen" , target = "lastModifiedWhen")
    ReplyVO toVO(ReplyEntity replyEntity);

    @Mapping(source = "content" , target = "content")
    @Mapping(expression = "java(toId())" , target = "replyId")
    @Mapping(ignore = true, target = "parentComment")
    @Mapping(ignore = true, target = "replies")
    @Mapping(ignore = true, target = "createdBy")
    ReplyEntity toEntity(ReplyDTO replyDTO);

    default String toId(){
        return AppUtil.generateIdentifier("RE");
    }

}
