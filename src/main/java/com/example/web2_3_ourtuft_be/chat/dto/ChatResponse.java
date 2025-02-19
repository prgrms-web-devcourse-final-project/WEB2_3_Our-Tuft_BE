package com.example.web2_3_ourtuft_be.chat.dto;

public record ChatResponse() {

    public record Message(String message, String sender) {
        public static Message of(String message, String sender) {
            return new Message(message, sender);
        }
    }
}
