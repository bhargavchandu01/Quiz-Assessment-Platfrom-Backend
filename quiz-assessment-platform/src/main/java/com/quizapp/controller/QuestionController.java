package com.quizapp.controller;

import com.quizapp.dto.ApiResponse;
import com.quizapp.dto.QuestionRequest;
import com.quizapp.entity.Question;
import com.quizapp.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Question>> createQuestion(@Valid @RequestBody QuestionRequest request) {
        Question question = questionService.createQuestion(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Question created successfully", question));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Question>>> getAllQuestions() {
        return ResponseEntity.ok(ApiResponse.success("Questions retrieved", questionService.getAllQuestions()));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<Question>>> getByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(ApiResponse.success("Questions retrieved",
                questionService.getQuestionsByCategory(categoryId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Question>> getQuestionById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Question retrieved", questionService.getQuestionById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Question>> updateQuestion(@PathVariable Long id,
                                                                 @Valid @RequestBody QuestionRequest request) {
        Question updated = questionService.updateQuestion(id, request);
        return ResponseEntity.ok(ApiResponse.success("Question updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.ok(ApiResponse.success("Question deleted successfully", null));
    }
}
