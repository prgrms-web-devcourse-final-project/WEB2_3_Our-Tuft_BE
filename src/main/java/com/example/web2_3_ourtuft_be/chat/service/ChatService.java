package com.example.web2_3_ourtuft_be.chat.service;

import com.example.web2_3_ourtuft_be.chat.dto.ChatRequest;
import com.example.web2_3_ourtuft_be.chat.dto.ChatResponse;
import com.example.web2_3_ourtuft_be.global.exception.messages.UnauthorizedMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

  private final SimpMessagingTemplate messagingTemplate;

  public void sendMessage(ChatRequest.Message message, SimpMessageHeaderAccessor headerAccessor) {
    //  String nickname = (String) headerAccessor.getSessionAttributes().get("nickname");
    // TODO : HandShakeInterceptor 를 사용해 JWT 의 유저 정보를 가져와 Sender 가 누군지 담아줄 예정.
    String nickname = "test";
    System.out.println(message.id());

    if (nickname == null) {
      messagingTemplate.convertAndSend(
          "/topic/errors", UnauthorizedMessages.AUTHENTICATION_REQUIRED);
      return;
    }

    // TODO : json 의 id 가 0일 경우는 로비로 채팅 전송 / 그 외의 경우는 방 으로 destination 설정
    String destination = message.id() == 0 ? "/topic/lobby" : "/topic/room/" + message.id();

    messagingTemplate.convertAndSend(
        destination, ChatResponse.Message.of(message.message(), nickname));
  }
}
