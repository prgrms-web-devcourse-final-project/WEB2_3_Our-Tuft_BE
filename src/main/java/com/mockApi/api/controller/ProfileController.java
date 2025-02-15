package com.mockApi.api.controller;

import com.mockApi.api.dto.ProfileRequestDto;
import com.mockApi.api.dto.ProfileResponseDto;
import com.mockApi.api.dto.ResponseMessageDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Objects;

@RestController
public class ProfileController {

    @GetMapping("/my")
    public ResponseEntity<ProfileResponseDto> myProfile() {

        ProfileResponseDto.EquipsDto equips = new ProfileResponseDto.EquipsDto(1,2,3,4,5);
        ProfileResponseDto response = new ProfileResponseDto("tester1", "hello!", 4, 15, 465, 500, 93.0, equips);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my/items")
    public ResponseEntity<ProfileResponseDto.Inventory> myItems() {

        List<Integer> hairs = List.of(1,2,3);
        List<Integer> eyes = List.of(3,4);
        List<Integer> mouses = List.of(2,5);
        List<Integer> skins = List.of(1,2);
        List<Integer> colors = List.of(5);

        ProfileResponseDto.Inventory response =  new ProfileResponseDto.Inventory(hairs,eyes,mouses,skins,colors);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/my")
    public ResponseEntity<ResponseMessageDto> updateProfile(@RequestBody ProfileRequestDto request) {
        String msg = validateProfile(request);
        if (msg != null) {
            return ResponseEntity.badRequest().body(new ResponseMessageDto(msg));
        }

        return ResponseEntity.ok(new ResponseMessageDto("프로필 변경 성공"));
    }

    private String validateProfile(ProfileRequestDto requestDto) {

        if (requestDto.getIntroduce() != null && requestDto.getIntroduce().length() > 100) {
            return "소개 글자 수는 최대 100자까지 가능합니다.";
        }
        if (requestDto.getHair() == 0 || requestDto.getEye() == 0 ||
                requestDto.getMouse() == 0 || requestDto.getSkin() == 0 ||
                requestDto.getNickColor() == 0) {
            return "아이템을 장착해야 합니다.";
        }

        return null; // 검증 통과
    }

    @PostMapping("/check/username")
    public ResponseEntity<ResponseMessageDto> checkUserName(@RequestBody ProfileRequestDto.CheckUserName request) {
        String username = request.getUsername();

        if (username.length() < 2 || username.length() > 10) {
            return ResponseEntity.badRequest().body(new ResponseMessageDto("글자 수 에러"));
        }

        if (username.equals("mock1")) {
            return ResponseEntity.badRequest().body(new ResponseMessageDto("이미 사용중인 이름"));
        }

        return ResponseEntity.ok(new ResponseMessageDto("사용 가능한 닉네임입니다."));
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<?> getUserProfile(@PathVariable String username) {
        if (!Objects.equals(username, "mock1")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessageDto("존재하지 않는 유저입니다."));
        }

        ProfileResponseDto.EquipsDto equips = new ProfileResponseDto.EquipsDto(1,2,3,4,5);
        ProfileResponseDto response = new ProfileResponseDto(username, "hello!", 4, 15, 465, 500, 93.0, equips);

        return ResponseEntity.ok(response);
    }
}
