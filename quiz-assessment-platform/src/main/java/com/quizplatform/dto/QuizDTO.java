package com.quizplatform.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

public class QuizDTO {
    private Long id;
    @NotBlank(message = "Quiz title is required")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String title;
    private String description;
    @Min(value = 1, message = "Time limit must be at least 1 minute")
    private Integer timeLimitMinutes;
    @Min(value = 0) @Max(value = 100)
    private Integer passingScore;
    private Boolean isActive;
    private Long createdById;
    private String createdByUsername;
    private List<QuestionDTO> questions;
    private Integer totalMarks;
    private Integer questionCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public QuizDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String t) { this.title = t; }
    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d; }
    public Integer getTimeLimitMinutes() { return timeLimitMinutes; }
    public void setTimeLimitMinutes(Integer t) { this.timeLimitMinutes = t; }
    public Integer getPassingScore() { return passingScore; }
    public void setPassingScore(Integer p) { this.passingScore = p; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean a) { this.isActive = a; }
    public Long getCreatedById() { return createdById; }
    public void setCreatedById(Long id) { this.createdById = id; }
    public String getCreatedByUsername() { return createdByUsername; }
    public void setCreatedByUsername(String u) { this.createdByUsername = u; }
    public List<QuestionDTO> getQuestions() { return questions; }
    public void setQuestions(List<QuestionDTO> q) { this.questions = q; }
    public Integer getTotalMarks() { return totalMarks; }
    public void setTotalMarks(Integer t) { this.totalMarks = t; }
    public Integer getQuestionCount() { return questionCount; }
    public void setQuestionCount(Integer q) { this.questionCount = q; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime c) { this.createdAt = c; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime u) { this.updatedAt = u; }
}
