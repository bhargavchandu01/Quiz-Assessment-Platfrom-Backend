package com.quizplatform.controller;

import com.quizplatform.dto.QuizDTO;
import com.quizplatform.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quizzes")

public class QuizController {

    @org.springframework.beans.factory.annotation.Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    private QuizService quizService;

    /**
     * GET /api/quizzes
     * List all active quizzes (any authenticated user).
     */
    @GetMapping
    public ResponseEntity<List<QuizDTO>> getAllQuizzes() {
        return ResponseEntity.ok(quizService.getAllActiveQuizzes());
    }

    /**
     * GET /api/quizzes/all
     * List all quizzes including inactive (Admin/Teacher only).
     */
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<QuizDTO>> getAllIncludingInactive() {
        return ResponseEntity.ok(quizService.getAllQuizzes());
    }

    /**
     * GET /api/quizzes/my
     * List quizzes created by the current teacher.
     */
    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<QuizDTO>> getMyQuizzes() {
        return ResponseEntity.ok(quizService.getMyQuizzes());
    }

    /**
     * GET /api/quizzes/search?keyword=...
     * Search active quizzes by title/description.
     */
    @GetMapping("/search")
    public ResponseEntity<List<QuizDTO>> searchQuizzes(
            @RequestParam String keyword) {
        return ResponseEntity.ok(quizService.searchQuizzes(keyword));
    }

    /**
     * GET /api/quizzes/{id}
     * Get quiz details (questions shown; answers hidden from students).
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuizDTO> getQuizById(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.getQuizById(id));
    }

    /**
     * POST /api/quizzes
     * Create a new quiz (Teacher/Admin only).
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<QuizDTO> createQuiz(@Valid @RequestBody QuizDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quizService.createQuiz(dto));
    }

    /**
     * PUT /api/quizzes/{id}
     * Update quiz metadata (owner or Admin).
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<QuizDTO> updateQuiz(
            @PathVariable Long id,
            @Valid @RequestBody QuizDTO dto) {
        return ResponseEntity.ok(quizService.updateQuiz(id, dto));
    }

    /**
     * PATCH /api/quizzes/{id}/toggle
     * Toggle active/inactive status.
     */
    @PatchMapping("/{id}/toggle")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<QuizDTO> toggleActive(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.toggleActive(id));
    }

    /**
     * DELETE /api/quizzes/{id}
     * Delete a quiz (owner or Admin).
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<Map<String, String>> deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.ok(Map.of("message", "Quiz deleted successfully"));
    }
}
