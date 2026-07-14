package com.quizplatform.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "student_answers")
public class StudentAnswer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result_id", nullable = false)
    private Result result;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "selected_answer", length = 1)
    private String selectedAnswer;

    @Column(name = "is_correct")    private Boolean isCorrect;
    @Column(name = "marks_obtained") private Integer marksObtained;

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final StudentAnswer a = new StudentAnswer();
        public Builder result(Result v) { a.result = v; return this; }
        public Builder question(Question v) { a.question = v; return this; }
        public Builder selectedAnswer(String v) { a.selectedAnswer = v; return this; }
        public Builder isCorrect(Boolean v) { a.isCorrect = v; return this; }
        public Builder marksObtained(Integer v) { a.marksObtained = v; return this; }
        public StudentAnswer build() { return a; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Result getResult() { return result; }
    public void setResult(Result r) { this.result = r; }
    public Question getQuestion() { return question; }
    public void setQuestion(Question q) { this.question = q; }
    public String getSelectedAnswer() { return selectedAnswer; }
    public void setSelectedAnswer(String s) { this.selectedAnswer = s; }
    public Boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(Boolean i) { this.isCorrect = i; }
    public Integer getMarksObtained() { return marksObtained; }
    public void setMarksObtained(Integer m) { this.marksObtained = m; }
}
