package com.quizplatform.controller;

import com.quizplatform.dto.QuestionDTO;
import com.quizplatform.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")

public class QuestionController {

    @org.springframework.beans.factory.annotation.Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    private QuestionService questionService;

    /**
     * GET /api/questions/quiz/{quizId}
     * Get all questions for a quiz.
     * Students see questions without correct answers.
     */
    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<List<QuestionDTO>> getQuestionsByQuiz(@PathVariable Long quizId) {
        return ResponseEntity.ok(questionService.getQuestionsByQuiz(quizId));
    }

    /**
     * GET /api/questions/{id}
     * Get a single question by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestion(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.getQuestionById(id));
    }

    /**
     * POST /api/questions/quiz/{quizId}
     * Add a question to a quiz (Teacher/Admin only).
     */
    @PostMapping("/quiz/{quizId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<QuestionDTO> addQuestion(
            @PathVariable Long quizId,
            @Valid @RequestBody QuestionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(questionService.addQuestion(quizId, dto));
    }

    /**
     * POST /api/questions/quiz/{quizId}/bulk
     * Add multiple questions at once (Teacher/Admin only).
     */
    @PostMapping("/quiz/{quizId}/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<QuestionDTO>> bulkAddQuestions(
            @PathVariable Long quizId,
            @RequestBody List<@Valid QuestionDTO> dtos) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(questionService.bulkAddQuestions(quizId, dtos));
    }

    /**
     * PUT /api/questions/{id}
     * Update a question (owner or Admin).
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<QuestionDTO> updateQuestion(
            @PathVariable Long id,
            @Valid @RequestBody QuestionDTO dto) {
        return ResponseEntity.ok(questionService.updateQuestion(id, dto));
    }

    /**
     * DELETE /api/questions/{id}
     * Delete a question (owner or Admin).
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<Map<String, String>> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.ok(Map.of("message", "Question deleted successfully"));
    }
}
