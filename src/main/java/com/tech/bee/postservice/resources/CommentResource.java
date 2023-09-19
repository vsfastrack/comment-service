package com.tech.bee.postservice.resources;

import com.tech.bee.postservice.annotation.TransactionId;
import com.tech.bee.postservice.common.ApiResponseDTO;
import com.tech.bee.postservice.constants.ApiConstants;
import com.tech.bee.postservice.dto.CommentDTO;
import com.tech.bee.postservice.dto.PostDTO;
import com.tech.bee.postservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
