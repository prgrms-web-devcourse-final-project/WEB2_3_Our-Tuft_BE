package com.example.web2_3_ourtuft_be.websockettest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.example.web2_3_ourtuft_be.chat.dto.ChatRequest;
import com.example.web2_3_ourtuft_be.chat.dto.ChatResponse;
import com.example.web2_3_ourtuft_be.chat.service.ChatService;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class WebSocketTest {

  @InjectMocks private ChatService chatService;

  @Mock private SimpMessagingTemplate messagingTemplate;

  @Mock private SimpMessageHeaderAccessor headerAccessor;

  @Test
  void ifNotIncludeNickname() {
    // given
    ChatRequest.Message message = new ChatRequest.Message(1L, "안녕하세요");
    when(headerAccessor.getSessionAttributes()).thenReturn(new HashMap<>());

    // when
    chatService.sendMessage(message, headerAccessor);

    // then
    // MessagingTemplate 가 호출되지 않았어야 한다
    verify(messagingTemplate, never()).convertAndSend(anyString(), anyString());
  }

  @Test
  void IfIncludeNickname() {
    // given
    ChatRequest.Message message = new ChatRequest.Message(1L, "안녕하세요");
    Map<String, Object> sessionAttributes = new HashMap<>();
    sessionAttributes.put("nickname", "테스트 유저");

    when(headerAccessor.getSessionAttributes()).thenReturn(sessionAttributes);

    // when
    chatService.sendMessage(message, headerAccessor);

    // then
    // messagingTemplate 가 호출되어야 한다
    verify(messagingTemplate).convertAndSend(eq("/topic/room/1"), any(ChatResponse.Message.class));
  }
}
