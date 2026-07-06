package com.quizplatform.controller;

import com.quizplatform.dto.QuizSubmissionDTO;
import com.quizplatform.dto.ResultDTO;
import com.quizplatform.service.ResultService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")

public class ResultController {

    @org.springframework.beans.factory.annotation.Autowired
    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    private ResultService resultService;

    /**
     * POST /api/results/start/{quizId}
     * Start a quiz attempt — creates an in-progress result.
     */
    @PostMapping("/start/{quizId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ResultDTO> startQuiz(@PathVariable Long quizId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resultService.startQuiz(quizId));
    }

    /**
     * POST /api/results/submit
     * Submit answers and receive graded result.
     */
    @PostMapping("/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ResultDTO> submitQuiz(
            @Valid @RequestBody QuizSubmissionDTO submission) {
        return ResponseEntity.ok(resultService.submitQuiz(submission));
    }

    /**
     * GET /api/results/my
     * Get all results for the currently logged-in student.
     */
    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<ResultDTO>> getMyResults() {
        return ResponseEntity.ok(resultService.getMyResults());
    }

    /**
     * GET /api/results/{id}
     * Get a specific result by ID.
     * Students can only see their own; teachers/admin see all.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResultDTO> getResultById(@PathVariable Long id) {
        return ResponseEntity.ok(resultService.getResultById(id));
    }

    /**
     * GET /api/results/quiz/{quizId}
     * Get all results for a specific quiz (Teacher/Admin only).
     */
    @GetMapping("/quiz/{quizId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<ResultDTO>> getResultsByQuiz(@PathVariable Long quizId) {
        return ResponseEntity.ok(resultService.getResultsByQuiz(quizId));
    }

    /**
     * GET /api/results/quiz/{quizId}/leaderboard
     * Get ranked leaderboard for a quiz.
     */
    @GetMapping("/quiz/{quizId}/leaderboard")
    public ResponseEntity<List<ResultDTO>> getLeaderboard(@PathVariable Long quizId) {
        return ResponseEntity.ok(resultService.getLeaderboard(quizId));
    }
}
