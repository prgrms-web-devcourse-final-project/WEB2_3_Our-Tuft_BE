package com.example.web2_3_ourtuft_be.user.dto;

public record UserResponse() {

    public record GetUserByContext(Long userId, String role) {}
}
