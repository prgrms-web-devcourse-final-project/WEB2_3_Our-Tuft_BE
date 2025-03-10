package com.example.web2_3_ourtuft_be.coupon.controller;

import com.example.web2_3_ourtuft_be.coupon.service.EventService;
import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event")
public class EventController {

    private final EventService eventService;

    @PostMapping("/points")
    public ResponseEntity<GlobalResponse<String>> applyEvent(@AuthenticationPrincipal(expression = "user") User user){

         eventService.apply(user.getId());

        return ResponseEntity.ok(GlobalResponse.success("이벤트 당첨"));
    }
}
