package com.example.web2_3_ourtuft_be.room.dto;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidValueException;
import com.example.web2_3_ourtuft_be.global.exception.messages.BadRequestMessages;
import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizSetType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequestDto {

    @NotBlank(message = "방 제목은 필수 입력 항목입니다.") @Size(max = 50, message = "방 제목은 50자 이하여야 합니다.") private String roomName;

    private boolean disclosure;

    @Min(value = 4, message = "비밀번호는 최소 4자리 이상이어야 합니다.") @Max(value = 6, message = "비밀번호는 최대 6자리 이하이어야 합니다.") private Integer password;

    @Min(value = 5, message = "라운드는 최소 5 이상이어야 합니다.") private int round;

    private QuizSetType quizType;

    @Min(value = 5, message = "진행 시간은 최소 5초 이상이어야 합니다.") private int time;

    @Min(value = 2, message = "참가 인원은 최소 2명 이상이어야 합니다.") private int participant;

    public void validate() {
        if (!disclosure && password == null) {
            throw new InvalidValueException(BadRequestMessages.ROOM_PASSWORD);
        }
    }
}
