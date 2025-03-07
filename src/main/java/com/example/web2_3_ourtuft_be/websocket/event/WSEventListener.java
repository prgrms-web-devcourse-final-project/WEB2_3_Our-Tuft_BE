package com.example.web2_3_ourtuft_be.websocket.event;

import com.example.web2_3_ourtuft_be.websocket.service.WSGameService;
import com.example.web2_3_ourtuft_be.websocket.service.WSRoomService;
import com.example.web2_3_ourtuft_be.websocket.service.WebSocketService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WSEventListener {

    private final WSRoomService wsRoomService;
    private final WebSocketService webSocketService;
    private final WSGameService wsGameService;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(event.getMessage());

        String roomId =
                (String) Objects.requireNonNull(accessor.getSessionAttributes()).get("roomId");
        String userId = (String) accessor.getSessionAttributes().get("userId");
        String username = (String) accessor.getSessionAttributes().get("username");

        wsRoomService.removePlayer(roomId, userId, username);
        log.info("roomId: {}, userId: {} disconnected", roomId, userId);
    }

    @EventListener
    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(event.getMessage());

        String destination = accessor.getDestination();

        if (Objects.requireNonNull(destination).contains("/topic/room/")) {
            String roomId = parseRoomId(Objects.requireNonNull(destination));
            Objects.requireNonNull(accessor.getSessionAttributes()).put("roomId", roomId);

            log.info("Subscribed to room {}", roomId);
        }
    }

    @EventListener
    public void handleWebSocketUnsubscribeListener(SessionUnsubscribeEvent event) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(event.getMessage());

        String roomId =
                (String) Objects.requireNonNull(accessor.getSessionAttributes()).get("roomId");
        String userId = (String) accessor.getSessionAttributes().get("userId");
        String username = (String) accessor.getSessionAttributes().get("username");

        wsRoomService.removePlayer(roomId, userId, username);
        log.info("roomId: {}, userId: {} unsubscribe", roomId, userId);
    }

    private String parseRoomId(String destination) {
        String[] parts = destination.split("/");
        return parts[parts.length - 1];
    }
}
