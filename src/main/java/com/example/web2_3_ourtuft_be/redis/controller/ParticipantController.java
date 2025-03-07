package com.example.web2_3_ourtuft_be.redis.controller;

import com.example.web2_3_ourtuft_be.redis.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/participants")
public class ParticipantController {

    private final ParticipantService participantService;
}
