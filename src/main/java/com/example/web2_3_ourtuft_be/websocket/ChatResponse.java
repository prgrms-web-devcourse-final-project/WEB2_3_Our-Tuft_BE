package com.example.web2_3_ourtuft_be.websocket;

public record ChatResponse() {

    public record Send(String sender, String message) {}
}
