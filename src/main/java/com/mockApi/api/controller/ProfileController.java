package com.mockApi.api.controller;

import com.mockApi.api.dto.ProfileRequestDto;
import com.mockApi.api.dto.ProfileResponseDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<String> updateProfile(@RequestBody ProfileRequestDto request) {
        String msg = validateProfile(request);
        if (msg != null) {
            return ResponseEntity.badRequest().body(msg);
        }

        return ResponseEntity.ok("프로필 변경 성공");
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
    public ResponseEntity<String> checkUserName(@RequestBody ProfileRequestDto.CheckUserName request) {
        String username = request.getUsername();

        if (username.length() < 2 || username.length() > 10) {
            return ResponseEntity.badRequest().body("글자 수 에러");
        }

        if (username.equals("mock1")) {
            return ResponseEntity.badRequest().body("이미 사용중인 이름");
        }

        return ResponseEntity.ok("사용 가능한 닉네임입니다.");
    }

}
