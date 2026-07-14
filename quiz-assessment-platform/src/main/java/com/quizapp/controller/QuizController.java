package com.quizapp.controller;

import com.quizapp.dto.ApiResponse;
import com.quizapp.dto.QuizRequest;
import com.quizapp.entity.Quiz;
import com.quizapp.service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Quiz>> createQuiz(@Valid @RequestBody QuizRequest request) {
        Quiz quiz = quizService.createQuiz(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Quiz created successfully", quiz));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Quiz>>> getAllQuizzes() {
        return ResponseEntity.ok(ApiResponse.success("Quizzes retrieved", quizService.getActiveQuizzes()));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Quiz>>> getAllQuizzesAdmin() {
        return ResponseEntity.ok(ApiResponse.success("All quizzes retrieved", quizService.getAllQuizzes()));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<Quiz>>> getByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(ApiResponse.success("Quizzes retrieved",
                quizService.getQuizzesByCategory(categoryId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Quiz>> getQuizById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Quiz retrieved", quizService.getQuizById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Quiz>> updateQuiz(@PathVariable Long id,
                                                         @Valid @RequestBody QuizRequest request) {
        Quiz updated = quizService.updateQuiz(id, request);
        return ResponseEntity.ok(ApiResponse.success("Quiz updated successfully", updated));
    }

    @PatchMapping("/{id}/toggle")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Quiz>> toggleQuizStatus(@PathVariable Long id) {
        Quiz quiz = quizService.toggleQuizStatus(id);
        return ResponseEntity.ok(ApiResponse.success("Quiz status toggled", quiz));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.ok(ApiResponse.success("Quiz deleted successfully", null));
    }
}
