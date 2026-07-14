package com.quizapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class QuizAttemptRequest {

    @NotNull(message = "Quiz ID is required")
    private Long quizId;

    // questionId -> selectedOption (A/B/C/D)
    @NotNull(message = "Answers are required")
    private Map<Long, String> answers;
}
