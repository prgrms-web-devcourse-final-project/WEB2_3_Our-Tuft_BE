package com.example.web2_3_ourtuft_be.websocket.dto;

public record WebSocketResponse() {

    public record Send(String sender, String message) {
        public static Send of(String sender, String message) {
            return new Send(sender, message);
        }
    }

    public record SendEvent(String event) {
        public static SendEvent of(String event) {
            return new SendEvent(event);
        }
    }
}
