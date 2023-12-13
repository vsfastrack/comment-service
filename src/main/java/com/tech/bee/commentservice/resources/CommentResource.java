package com.tech.bee.commentservice.resources;

import com.tech.bee.commentservice.annotation.TransactionId;
import com.tech.bee.commentservice.common.ApiResponseDTO;
import com.tech.bee.commentservice.constants.ApiConstants;
import com.tech.bee.commentservice.dto.CommentDTO;
import com.tech.bee.commentservice.service.CommentService;
import com.tech.bee.commentservice.vo.ReplyVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = ApiConstants.PathConstants.PATH_COMMENT_RESOURCE)
public class CommentResource {

    private final CommentService commentService;

    @TransactionId
    @PostMapping
    public ResponseEntity<ApiResponseDTO> create(@RequestBody CommentDTO commentDTO){
        String commentId = commentService.create(commentDTO);
        return new ResponseEntity<>(ApiResponseDTO.builder().content(commentId).build() , HttpStatus.CREATED);
    }

    @TransactionId
    @GetMapping("/{commentIdentifier}")
    public ResponseEntity<ApiResponseDTO> findCommentDetails(@PathVariable("commentIdentifier") final String commentIdentifier){
        return new ResponseEntity<>(ApiResponseDTO.builder().content(commentService.getCommentDetails(commentIdentifier)).build(), HttpStatus.OK);
    }

    @TransactionId
    @GetMapping("/{postIdentifier}/details")
    public ResponseEntity<ApiResponseDTO> getCommentsByPostId(@PathVariable("postIdentifier") final String postIdentifier){
        return new ResponseEntity<>(ApiResponseDTO.builder().content(commentService.getCommentsByPostIdentifier(postIdentifier)).build(), HttpStatus.OK);
    }

    @TransactionId
    @PatchMapping("/{commentIdentifier}")
    public ResponseEntity<ApiResponseDTO> update(@PathVariable("commentIdentifier") final String commentIdentifier ,
                                                 @RequestBody CommentDTO commentDTO){
        commentService.modify(commentIdentifier , commentDTO);
        return new ResponseEntity<>(ApiResponseDTO.builder().build(), HttpStatus.NO_CONTENT);
    }

    @TransactionId
    @DeleteMapping("/{commentIdentifier}")
    public ResponseEntity<ApiResponseDTO> delete(@PathVariable("commentIdentifier") final String commentIdentifier){
        commentService.delete(commentIdentifier);
        return new ResponseEntity<>(ApiResponseDTO.builder().build(), HttpStatus.NO_CONTENT);
    }

    @TransactionId
    @GetMapping(value="/{commentId}/replies")
    public ResponseEntity<ApiResponseDTO> getRepliesByCommentId(@PathVariable("commentId")
                                                                final String commentId){
        List<ReplyVO> replies = commentService.getRepliesForComment(commentId);
        return new ResponseEntity<>(ApiResponseDTO.builder().content(replies).build() , HttpStatus.OK);
    }

}
