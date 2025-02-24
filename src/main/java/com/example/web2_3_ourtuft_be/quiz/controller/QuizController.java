package com.example.web2_3_ourtuft_be.quiz.controller;

import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.quiz.dto.QuizCategoryResponse;
import com.example.web2_3_ourtuft_be.quiz.dto.QuizSetSummaryResponse;
import com.example.web2_3_ourtuft_be.quiz.dto.RegistQuizSetRequest;
import com.example.web2_3_ourtuft_be.quiz.dto.RegistQuizSetResponse;
import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizSetType;
import com.example.web2_3_ourtuft_be.quiz.service.QuizCategoryService;
import com.example.web2_3_ourtuft_be.quiz.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/quizzes")
public class QuizController {

    private final QuizService quizService;
    private final QuizCategoryService quizCategoryService;

    @Operation(summary = "퀴즈 등록 API", description = "퀴즈를 등록합니다.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "성공")})
    @PostMapping()
    public GlobalResponse<RegistQuizSetResponse> registQuizset(
            @RequestBody RegistQuizSetRequest quizeSetRequest) {

        RegistQuizSetResponse resultData = quizService.registQuizSet(quizeSetRequest);
        return GlobalResponse.created(resultData);
    }

    @Operation(summary = "퀴즈세트 삭제 API", description = "등록된 퀴즈세트를 삭제합니다.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "성공")})
    @DeleteMapping("/{quizsetid}")
    public GlobalResponse<String> deleteQuizset(@PathVariable("quizsetid") Long quizSetId) {

        quizService.deleteQuizSetAndQuizzes(quizSetId);
        return GlobalResponse.success("퀴즈세트를 삭제 했습니다.");
    }

    @Operation(summary = "퀴즈 주제 카테고리 조회 API", description = "퀴즈 주제 카테고리를 조회합니다.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "성공")})
    @GetMapping()
    public GlobalResponse<List<QuizCategoryResponse>> getQuizSetList() {

        List<QuizCategoryResponse> quizSetList = quizCategoryService.getQuizCategories();
        return GlobalResponse.success(quizSetList);
    }
}
