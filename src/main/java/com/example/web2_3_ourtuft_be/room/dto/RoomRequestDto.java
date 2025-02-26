package com.example.web2_3_ourtuft_be.room.dto;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidValueException;
import com.example.web2_3_ourtuft_be.global.exception.messages.BadRequestMessages;
import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizSetType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RoomRequestDto {

    @NotBlank(message = "방 제목은 필수 입력 항목입니다.") @Size(max = 50, message = "방 제목은 50자 이하여야 합니다.") private String roomName;

    private boolean disclosure;

    private String password;

    @Min(value = 5, message = "라운드는 최소 5 이상이어야 합니다.") private int round;

    private QuizSetType gameType;

    @Min(value = 5, message = "진행 시간은 최소 5초 이상이어야 합니다.") private int time;

    @Min(value = 2, message = "참가 인원은 최소 2명 이상이어야 합니다.") private int participant;

    public RoomRequestDto(
            String roomName,
            boolean disclosure,
            String password,
            int round,
            QuizSetType gameType,
            int time) {
        this.roomName = roomName;
        this.disclosure = disclosure;
        this.password = password;
        this.round = round;
        this.gameType = gameType;
        this.time = time;

        validate();
    }

    public void validate() {

        if (!disclosure && password == null) {
            throw new InvalidValueException(BadRequestMessages.ROOM_PASSWORD);
        }

        if (disclosure && !password.isEmpty()) {
            throw new InvalidValueException(BadRequestMessages.ROOM_PASSWORD_DISCLOSURE);
        }

        if (!disclosure && password.length() != 4) {
            throw new InvalidValueException(BadRequestMessages.ROOM_PASSWORD_LENGTH);
        }

        if (!disclosure && !password.matches("^[0-9]+$")) {
            throw new InvalidValueException(BadRequestMessages.ROOM_PASSWORD_FORMAT);
        }
    }
}
