package com.example.web2_3_ourtuft_be.websocket;

public record ChatRequest() {

    public record Send(String room, String message) {}
}
