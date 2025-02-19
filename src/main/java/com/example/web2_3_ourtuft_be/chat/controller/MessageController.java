package com.example.web2_3_ourtuft_be.chat.controller;

import com.example.web2_3_ourtuft_be.chat.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    /*
        /sub/channel/12345      - 구독(channelId:12345)
        /pub/hello              - 메시지 발행
    */
    @MessageMapping("/hello")
    public void message(Message message) {

        simpMessageSendingOperations.convertAndSend(
                "/sub/channel/" + message.getChannelId(), message);
    }
}
