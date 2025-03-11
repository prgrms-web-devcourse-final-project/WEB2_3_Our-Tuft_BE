package com.example.web2_3_ourtuft_be.user.entity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidValueException;
import com.example.web2_3_ourtuft_be.user.value.Nickname;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NicknameTest {

    @Test
    @DisplayName("닉네임 null, 공백 예외 발생 테스트")
    void invalidNicknameTest1() {
        // Given
        String blank = "";
        String n = null;

        // When & Then
        assertThrows(
                InvalidValueException.class,
                () -> {
                    new Nickname(blank);
                    new Nickname(n);
                });
    }

    @Test
    @DisplayName("영/한/숫자 이외의 문자 예외 발생 테스트")
    void invalidNicknameTest2() {
        // Given
        String input = "!#@ad";

        // When & Then
        assertThrows(
                InvalidValueException.class,
                () -> {
                    new Nickname(input);
                });
    }

    @Test
    @DisplayName("길이 예외 테스트(1글자)")
    void invalidNicknameTest3() {
        // Given
        String input = "a";

        // When & Then
        assertThrows(
                InvalidValueException.class,
                () -> {
                    new Nickname(input);
                });
    }

    @Test
    @DisplayName("길이 예외 테스트(11글자)")
    void invalidNicknameTest4() {
        // Given
        String input = "abcdefghqwe";

        // When & Then
        assertThrows(
                InvalidValueException.class,
                () -> {
                    new Nickname(input);
                });
    }

    @Test
    @DisplayName("정상 생성")
    void validNameTest() {
        // Given
        String input = "tester1";

        // When & Then
        assertDoesNotThrow(
                () -> {
                    new Nickname(input);
                });
    }
}
