package com.example.web2_3_ourtuft_be.chat.controller;

import com.example.web2_3_ourtuft_be.chat.dto.ChatRequest;
import com.example.web2_3_ourtuft_be.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

  private final ChatService chatService;

  @MessageMapping("/chat.sendMessage")
  public void sendMessage(
      @Payload final ChatRequest.Message message, SimpMessageHeaderAccessor headerAccessor) {
    chatService.sendMessage(message, headerAccessor);
  }
}
