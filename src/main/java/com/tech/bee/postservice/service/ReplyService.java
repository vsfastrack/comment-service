package com.tech.bee.postservice.service;

import com.tech.bee.postservice.dto.ReplyDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReplyService {

    public String create(ReplyDTO replyDTO){
        return "ABC";
    }
}
