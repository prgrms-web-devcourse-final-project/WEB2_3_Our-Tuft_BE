package com.example.web2_3_ourtuft_be.chat.dto;

public record ChatRequest() {

  public record Message(Long id, String message) {}
}
