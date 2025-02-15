package com.mockApi.api.controller;

import com.mockApi.api.dto.ProfileResponseDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.apache.coyote.Response;

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
}
