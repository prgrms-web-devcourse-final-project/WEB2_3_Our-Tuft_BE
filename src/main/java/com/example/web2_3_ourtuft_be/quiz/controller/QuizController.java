package com.example.web2_3_ourtuft_be.quiz.controller;

import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.quiz.dto.RegistQuizSetAndQuizzesRequest;
import com.example.web2_3_ourtuft_be.quiz.dto.RegistQuizSetAndQuizzesResponse;
import com.example.web2_3_ourtuft_be.quiz.dto.UpdateQuizSetandQuizzesRequest;
import com.example.web2_3_ourtuft_be.quiz.service.QuizService;
import com.example.web2_3_ourtuft_be.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/quizzes")
public class QuizController {

    private final QuizService quizService;

    @Operation(summary = "퀴즈 등록 API", description = "퀴즈를 등록합니다.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "성공")})
    @PostMapping()
    public ResponseEntity<GlobalResponse<RegistQuizSetAndQuizzesResponse>> registQuizset(
            @AuthenticationPrincipal(expression = "user") User user,
            @Valid @RequestBody RegistQuizSetAndQuizzesRequest registQuizSetAndQuizzesRequest) {

        Long creatorId = user.getId();

        RegistQuizSetAndQuizzesResponse registQuizSetAndQuizzesResponse =
                quizService.registQuizSet(creatorId, registQuizSetAndQuizzesRequest);

        return ResponseEntity.ok(GlobalResponse.created(registQuizSetAndQuizzesResponse));
    }

    @Operation(summary = "퀴즈세트 삭제 API", description = "등록된 퀴즈세트를 삭제합니다.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "성공")})
    @DeleteMapping("/{quizsetid}")
    public ResponseEntity<GlobalResponse<String>> deleteQuizset(
            @PathVariable("quizsetid") Long quizSetId) {

        quizService.deleteQuizSetAndQuizzes(quizSetId);

        return ResponseEntity.ok(GlobalResponse.success("퀴즈세트를 삭제 했습니다."));
    }

    @Operation(summary = "퀴즈세트, 퀴즈 수정 API", description = "전달받은 퀴즈,퀴즈세트 데이터로 수정 합니다.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
    @PutMapping("/{quizsetid}")
    public ResponseEntity<GlobalResponse<String>> updateQuizSetAndQuizzes(
            @PathVariable("quizsetid") Long quizSetId,
            @RequestBody UpdateQuizSetandQuizzesRequest request) {

        quizService.updateQuizSetAndQuizzes(quizSetId, request);

        return ResponseEntity.ok(GlobalResponse.success(null));
    }
}
