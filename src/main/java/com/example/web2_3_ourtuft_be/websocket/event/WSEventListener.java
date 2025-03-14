package com.example.web2_3_ourtuft_be.websocket.event;

import com.example.web2_3_ourtuft_be.websocket.service.WSRoomService;
import java.util.Map;
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

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        Map<String, Object> sessionAttributes =
                Objects.requireNonNull(accessor.getSessionAttributes());

        String currentRoomId = (String) sessionAttributes.get("roomId");
        String userId = (String) sessionAttributes.get("userId");
        String username = (String) sessionAttributes.get("username");

        if (currentRoomId != null) {
            wsRoomService.removePlayer(currentRoomId, userId, username);
            sessionAttributes.remove("roomId");

            log.info(
                    "roomId: {}, userId: {}, username: {} disconnected",
                    currentRoomId,
                    userId,
                    username);
        } else {
            log.info("User {} ({}) disconnected (no subscribed room)", userId, username);
        }
    }

    @EventListener
    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String destination = accessor.getDestination();
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

        String newRoomId = parseRoomId(destination);
        String currentRoomId = (String) sessionAttributes.get("roomId");
        String flag = (String) sessionAttributes.get("changeRoomToGame");

        if (currentRoomId != null && !flag.equals("true")) {
            if (!newRoomId.equals(currentRoomId)) {
                wsRoomService.removePlayer(
                        currentRoomId,
                        (String) sessionAttributes.get("userId"),
                        (String) sessionAttributes.get("username"));
                sessionAttributes.put("roomId", newRoomId);
                log.info("Room changed from {} to {}", currentRoomId, newRoomId);
            } else {
                log.info("Already subscribed to room {}", currentRoomId);
            }
        } else {
            sessionAttributes.put("roomId", newRoomId);
            log.info("Subscribed to room {}", newRoomId);
        }
    }

    @EventListener
    public void handleWebSocketUnsubscribeListener(SessionUnsubscribeEvent event) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        Map<String, Object> sessionAttributes =
                Objects.requireNonNull(accessor.getSessionAttributes());

        String roomId = (String) sessionAttributes.get("roomId");
        String userId = (String) sessionAttributes.get("userId");
        String username = (String) sessionAttributes.get("username");
        String flag = (String) sessionAttributes.get("changeRoomToGame");

        if (flag == null) flag = "false";

        if (roomId != null && flag.equals("false")) {
            wsRoomService.removePlayer(roomId, userId, username);
            log.info("roomId: {}, userId: {} unsubscribe", roomId, userId);
            sessionAttributes.remove("roomId");
        }
    }

    private String parseRoomId(String destination) {
        String[] parts = destination.split("/");

        return parts[parts.length - 1];
    }

}
