package com.example.web2_3_ourtuft_be.game.service;

import com.example.web2_3_ourtuft_be.game.dto.OXResponseDto;
import org.springframework.stereotype.Service;

@Service
public class OXQuizService {

    public OXResponseDto checkAnswer(Long roomId, Long userId, int round, String userAnswer) {

        String correctAnswer = getCorrectAnswer(roomId, round);
        boolean isCorrect = correctAnswer.equalsIgnoreCase(userAnswer);
        return new OXResponseDto(userId, isCorrect);
    }

    public String getCorrectAnswer(Long roomId, int round) {
        // TODO: redis에서 해당 roomId, round에 맞는 정답 가져오기
        // 지금은 무조건 정답 X로 보내겠습니다.
        return "X";
    }
}
