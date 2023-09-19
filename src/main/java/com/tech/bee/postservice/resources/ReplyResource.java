package com.tech.bee.postservice.resources;

import com.tech.bee.postservice.annotation.TransactionId;
import com.tech.bee.postservice.common.ApiResponseDTO;
import com.tech.bee.postservice.constants.ApiConstants;
import com.tech.bee.postservice.dto.ReplyDTO;
import com.tech.bee.postservice.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = ApiConstants.PathConstants.PATH_REPLY_RESOURCE)
public class ReplyResource {

    private ReplyService replyService;

    @TransactionId
    @PostMapping
    public ResponseEntity<ApiResponseDTO> create(@RequestBody ReplyDTO replyDTO){
        String commentId = replyService.create(replyDTO);
        return new ResponseEntity<>(ApiResponseDTO.builder().content(commentId).build() , HttpStatus.CREATED);
    }

}
