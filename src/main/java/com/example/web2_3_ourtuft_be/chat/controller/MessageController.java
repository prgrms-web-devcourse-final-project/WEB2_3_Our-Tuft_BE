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

        /sub/channel/12345      - 구독(channelId:"12345",channelId:"lobby" )
        /pub/lobby              - 대기실 메시지 발행
        /pub/room               - 룸 메세지 발행

    */

    /**
     * @param message { "type": "new", "sender" : "tester", "channelId": "lobby", "data": "tester님이
     *     접속 하셨습니다." }
     */
    @MessageMapping("/lobby")
    public void joinLobby(Message message) {
        simpMessageSendingOperations.convertAndSend("/sub/channel/lobby", message);
    }

    /**
     * @param message { "type": "chat", "sender" : "tester", "channelId": "lobby", "data": "방 파주세요.
     *     들어가겠습니다." }
     */
    @MessageMapping("/lobby/chat")
    public void lobbyChat(Message message) {
        System.out.println(
                "channelId : " + message.getChannelId() + "," + "data: " + message.getData());
        simpMessageSendingOperations.convertAndSend("/sub/channel/lobby", message);
    }

    /**
     * @param message { "type": "new", "sender" : "tester", "channelId": "12345", "data": "tester님이
     *     입장 하셨습니다." }
     */
    @MessageMapping("/room")
    public void joinRoom(Message message) {
        System.out.println("channelId :" + message.getChannelId() + "구독");
        simpMessageSendingOperations.convertAndSend(
                "/sub/channel/" + message.getChannelId(), message);
    }

    /**
     * @param message { "type": "new", "sender" : "tester", "channelId": "12345", "data": "ㄹㄷ 안하면
     *     강퇴" }
     */
    @MessageMapping("/room/chat")
    public void roomChat(Message message) {
        System.out.println(
                "channelId :" + message.getChannelId() + "," + "data: " + message.getData());
        simpMessageSendingOperations.convertAndSend(
                "/sub/channel/" + message.getChannelId(), message);
    }

    /**
     * @param message { "type": "new", "sender" : "tester", "channelId": "12345", "data": "tester님이
     *     나가셨습니다." }
     */
    @MessageMapping("/room/exit")
    public void roomExit(Message message) {
        System.out.println(
                "channelId :" + message.getChannelId() + "," + "data: " + message.getData());
        simpMessageSendingOperations.convertAndSend(
                "/sub/channel/" + message.getChannelId(), message);
    }
}
