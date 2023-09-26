package com.tech.bee.commentservice.resources;

import com.tech.bee.commentservice.annotation.TransactionId;
import com.tech.bee.commentservice.common.ApiResponseDTO;
import com.tech.bee.commentservice.constants.ApiConstants;
import com.tech.bee.commentservice.dto.ReplyDTO;
import com.tech.bee.commentservice.enums.Enums;
import com.tech.bee.commentservice.service.ReplyService;
import com.tech.bee.commentservice.vo.ReplyVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = ApiConstants.PathConstants.PATH_REPLY_RESOURCE)
@Secured({ApiConstants.RoleConstants.ROLE_AUTHOR ,ApiConstants.RoleConstants.ROLE_USER })
public class ReplyResource {

    private final ReplyService replyService;

    @TransactionId
    @PostMapping
    public ResponseEntity<ApiResponseDTO> create(@RequestBody ReplyDTO replyDTO){
        String commentId = replyService.create(replyDTO);
        return new ResponseEntity<>(ApiResponseDTO.builder().content(commentId).build() , HttpStatus.CREATED);
    }

    @TransactionId
    @GetMapping(value="/{replyIdentifier}")
    public ResponseEntity<ApiResponseDTO> getReplyDetails(@PathVariable("replyIdentifier")
                                                              final String replyIdentifier){
        ReplyVO replyDetails = replyService.getReplyDetails(replyIdentifier);
        return new ResponseEntity<>(ApiResponseDTO.builder().content(replyDetails).build() , HttpStatus.OK);
    }

    @TransactionId
    @GetMapping(value="/{replyIdentifier}/children/{numOfReplies}")
    public ResponseEntity<ApiResponseDTO> getChildrenReplyDetails(@PathVariable("replyIdentifier")final String replyIdentifier,
                                                          @PathVariable("numOfReplies") final Integer numOfReplies ,
                                                          @RequestParam(name ="sortDir" , defaultValue = "DESC") final Enums.SortDirection sortDir ,
                                                          @RequestParam(name="sortKey" , defaultValue = "createdWhen") final String sortKey){
        List<ReplyVO> childReplyDetails = replyService.getAllChildReplies(replyIdentifier);
        return new ResponseEntity<>(ApiResponseDTO.builder().content(childReplyDetails).build() , HttpStatus.OK);
    }

    @TransactionId
    @PatchMapping(value="/{replyIdentifier}")
    public ResponseEntity<ApiResponseDTO> update(@PathVariable("replyIdentifier")final String replyIdentifier ,
                                                      @RequestBody ReplyDTO replyDTO){
        replyService.modify(replyIdentifier , replyDTO);
        return new ResponseEntity<>(ApiResponseDTO.builder().build(), HttpStatus.NO_CONTENT);
    }

    @TransactionId
    @DeleteMapping(value="/{replyIdentifier}")
    public ResponseEntity<ApiResponseDTO> delete(@PathVariable("replyIdentifier")final String replyIdentifier){
        replyService.delete(replyIdentifier);
        return new ResponseEntity<>(ApiResponseDTO.builder().build(), HttpStatus.NO_CONTENT);
    }
}
