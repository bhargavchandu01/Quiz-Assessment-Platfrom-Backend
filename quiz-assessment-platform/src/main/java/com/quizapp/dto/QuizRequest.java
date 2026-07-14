package com.quizapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class QuizRequest {

    @NotBlank(message = "Quiz title is required")
    private String title;

    private String description;

    @Min(value = 1, message = "Number of questions must be at least 1")
    private int numberOfQuestions;

    @Min(value = 1, message = "Max marks must be at least 1")
    private int maxMarks;

    @Min(value = 1, message = "Time limit must be at least 1 minute")
    private int timeLimitMinutes;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private List<Long> questionIds;
}
