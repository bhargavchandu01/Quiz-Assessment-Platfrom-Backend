package com.quizapp.controller;

import com.quizapp.dto.ApiResponse;
import com.quizapp.dto.QuizAttemptRequest;
import com.quizapp.entity.Result;
import com.quizapp.service.ResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;

    @PostMapping("/attempt")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<Result>> attemptQuiz(@Valid @RequestBody QuizAttemptRequest request) {
        Result result = resultService.attemptQuiz(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Quiz submitted successfully", result));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<Result>>> getMyResults() {
        return ResponseEntity.ok(ApiResponse.success("Results retrieved", resultService.getMyResults()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<ApiResponse<Result>> getResultById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Result retrieved", resultService.getResultById(id)));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Result>>> getResultsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success("Results retrieved", resultService.getResultsByUserId(userId)));
    }

    @GetMapping("/quiz/{quizId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Result>>> getResultsByQuiz(@PathVariable Long quizId) {
        return ResponseEntity.ok(ApiResponse.success("Results retrieved", resultService.getResultsByQuizId(quizId)));
    }

    @GetMapping("/quiz/{quizId}/average")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Double>> getAverageScore(@PathVariable Long quizId) {
        return ResponseEntity.ok(ApiResponse.success("Average score retrieved",
                resultService.getAverageScoreForQuiz(quizId)));
    }
}
