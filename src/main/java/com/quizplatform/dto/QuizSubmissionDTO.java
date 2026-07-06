package com.quizplatform.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

public class QuizSubmissionDTO {
    @NotNull(message = "Quiz ID is required")
    private Long quizId;
    @NotNull(message = "Result ID is required")
    private Long resultId;
    private Map<Long, String> answers;
    private Integer timeTakenSeconds;

    public QuizSubmissionDTO() {}

    public Long getQuizId() { return quizId; }
    public void setQuizId(Long q) { this.quizId = q; }
    public Long getResultId() { return resultId; }
    public void setResultId(Long r) { this.resultId = r; }
    public Map<Long, String> getAnswers() { return answers; }
    public void setAnswers(Map<Long, String> a) { this.answers = a; }
    public Integer getTimeTakenSeconds() { return timeTakenSeconds; }
    public void setTimeTakenSeconds(Integer t) { this.timeTakenSeconds = t; }
}
