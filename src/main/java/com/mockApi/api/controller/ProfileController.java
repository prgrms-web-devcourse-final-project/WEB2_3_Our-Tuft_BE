package com.mockApi.api.controller;

import com.mockApi.api.dto.ProfileResponseDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

    @GetMapping("/my")
    public ResponseEntity<ProfileResponseDto> myProfile() {

        ProfileResponseDto.EquipsDto equips = new ProfileResponseDto.EquipsDto(1,2,3,4,5);
        ProfileResponseDto response = new ProfileResponseDto("tester1", "hello!", 4, 15, 465, 500, 93.0, equips);

        return ResponseEntity.ok(response);
    }
}
