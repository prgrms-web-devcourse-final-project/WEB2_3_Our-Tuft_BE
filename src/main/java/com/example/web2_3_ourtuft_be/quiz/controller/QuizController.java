package com.example.web2_3_ourtuft_be.quiz.controller;

import static com.example.web2_3_ourtuft_be.quiz.service.QuizService.createTestData;

import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.quiz.dto.QuizSetRequest;
import com.example.web2_3_ourtuft_be.quiz.entity.QuizSet;
import com.example.web2_3_ourtuft_be.quiz.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/quizzes")
public class QuizController {

    private final QuizService quizService;

    @Operation(summary = "퀴즈 등록 API", description = "퀴즈를 등록합니다.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "성공")})
    @PostMapping
    public ResponseEntity<GlobalResponse<QuizSet>> regQuizSet(
            @RequestBody QuizSetRequest quizSetRequest) {
        QuizSet resultData = quizService.createQuizSet(quizSetRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(GlobalResponse.created(resultData));
    }

    @Operation(summary = "퀴즈 더미 데이터 등록 API", description = "퀴즈 더미 데이터를 등록합니다.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "성공")})
    @PostMapping("/test")
    public ResponseEntity<GlobalResponse<QuizSet>> regTestQuizSet() {
        QuizSetRequest testData = createTestData();
        QuizSet resultData = quizService.createQuizSet(testData);
        return ResponseEntity.status(HttpStatus.CREATED).body(GlobalResponse.created(resultData));
    }
}
