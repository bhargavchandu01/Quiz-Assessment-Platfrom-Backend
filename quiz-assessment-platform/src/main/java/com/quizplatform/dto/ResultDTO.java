package com.quizplatform.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ResultDTO {
    private Long id;
    private Long studentId;
    private String studentUsername;
    private Long quizId;
    private String quizTitle;
    private Integer scoreObtained;
    private Integer totalMarks;
    private Double percentage;
    private Boolean passed;
    private Integer timeTakenSeconds;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;
    private List<AnswerDetailDTO> answerDetails;

    public ResultDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long s) { this.studentId = s; }
    public String getStudentUsername() { return studentUsername; }
    public void setStudentUsername(String s) { this.studentUsername = s; }
    public Long getQuizId() { return quizId; }
    public void setQuizId(Long q) { this.quizId = q; }
    public String getQuizTitle() { return quizTitle; }
    public void setQuizTitle(String q) { this.quizTitle = q; }
    public Integer getScoreObtained() { return scoreObtained; }
    public void setScoreObtained(Integer s) { this.scoreObtained = s; }
    public Integer getTotalMarks() { return totalMarks; }
    public void setTotalMarks(Integer t) { this.totalMarks = t; }
    public Double getPercentage() { return percentage; }
    public void setPercentage(Double p) { this.percentage = p; }
    public Boolean getPassed() { return passed; }
    public void setPassed(Boolean p) { this.passed = p; }
    public Integer getTimeTakenSeconds() { return timeTakenSeconds; }
    public void setTimeTakenSeconds(Integer t) { this.timeTakenSeconds = t; }
    public String getStatus() { return status; }
    public void setStatus(String s) { this.status = s; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime s) { this.startedAt = s; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime s) { this.submittedAt = s; }
    public List<AnswerDetailDTO> getAnswerDetails() { return answerDetails; }
    public void setAnswerDetails(List<AnswerDetailDTO> a) { this.answerDetails = a; }

    // Builder
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final ResultDTO dto = new ResultDTO();
        public Builder id(Long v) { dto.id = v; return this; }
        public Builder studentId(Long v) { dto.studentId = v; return this; }
        public Builder studentUsername(String v) { dto.studentUsername = v; return this; }
        public Builder quizId(Long v) { dto.quizId = v; return this; }
        public Builder quizTitle(String v) { dto.quizTitle = v; return this; }
        public Builder scoreObtained(Integer v) { dto.scoreObtained = v; return this; }
        public Builder totalMarks(Integer v) { dto.totalMarks = v; return this; }
        public Builder percentage(Double v) { dto.percentage = v; return this; }
        public Builder passed(Boolean v) { dto.passed = v; return this; }
        public Builder timeTakenSeconds(Integer v) { dto.timeTakenSeconds = v; return this; }
        public Builder status(String v) { dto.status = v; return this; }
        public Builder startedAt(LocalDateTime v) { dto.startedAt = v; return this; }
        public Builder submittedAt(LocalDateTime v) { dto.submittedAt = v; return this; }
        public Builder answerDetails(List<AnswerDetailDTO> v) { dto.answerDetails = v; return this; }
        public ResultDTO build() { return dto; }
    }

    public static class AnswerDetailDTO {
        private Long questionId;
        private String questionText;
        private String selectedAnswer;
        private String correctAnswer;
        private Boolean isCorrect;
        private Integer marks;
        private Integer marksObtained;

        public AnswerDetailDTO() {}

        public Long getQuestionId() { return questionId; }
        public void setQuestionId(Long q) { this.questionId = q; }
        public String getQuestionText() { return questionText; }
        public void setQuestionText(String q) { this.questionText = q; }
        public String getSelectedAnswer() { return selectedAnswer; }
        public void setSelectedAnswer(String s) { this.selectedAnswer = s; }
        public String getCorrectAnswer() { return correctAnswer; }
        public void setCorrectAnswer(String c) { this.correctAnswer = c; }
        public Boolean getIsCorrect() { return isCorrect; }
        public void setIsCorrect(Boolean i) { this.isCorrect = i; }
        public Integer getMarks() { return marks; }
        public void setMarks(Integer m) { this.marks = m; }
        public Integer getMarksObtained() { return marksObtained; }
        public void setMarksObtained(Integer m) { this.marksObtained = m; }

        public static Builder builder() { return new Builder(); }
        public static class Builder {
            private final AnswerDetailDTO dto = new AnswerDetailDTO();
            public Builder questionId(Long v) { dto.questionId = v; return this; }
            public Builder questionText(String v) { dto.questionText = v; return this; }
            public Builder selectedAnswer(String v) { dto.selectedAnswer = v; return this; }
            public Builder correctAnswer(String v) { dto.correctAnswer = v; return this; }
            public Builder isCorrect(Boolean v) { dto.isCorrect = v; return this; }
            public Builder marks(Integer v) { dto.marks = v; return this; }
            public Builder marksObtained(Integer v) { dto.marksObtained = v; return this; }
            public AnswerDetailDTO build() { return dto; }
        }
    }
}
