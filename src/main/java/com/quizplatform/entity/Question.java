package com.quizplatform.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "option_a", nullable = false) private String optionA;
    @Column(name = "option_b", nullable = false) private String optionB;
    @Column(name = "option_c", nullable = false) private String optionC;
    @Column(name = "option_d", nullable = false) private String optionD;

    @Column(name = "correct_answer", nullable = false, length = 1)
    private String correctAnswer;

    @Column(nullable = false)
    private Integer marks = 5;

    @Column(name = "question_order")
    private Integer questionOrder;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StudentAnswer> studentAnswers = new ArrayList<>();

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Question q = new Question();
        public Builder quiz(Quiz v) { q.quiz = v; return this; }
        public Builder questionText(String v) { q.questionText = v; return this; }
        public Builder optionA(String v) { q.optionA = v; return this; }
        public Builder optionB(String v) { q.optionB = v; return this; }
        public Builder optionC(String v) { q.optionC = v; return this; }
        public Builder optionD(String v) { q.optionD = v; return this; }
        public Builder correctAnswer(String v) { q.correctAnswer = v; return this; }
        public Builder marks(Integer v) { q.marks = v; return this; }
        public Builder questionOrder(Integer v) { q.questionOrder = v; return this; }
        public Question build() { return q; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz q) { this.quiz = q; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String t) { this.questionText = t; }
    public String getOptionA() { return optionA; }
    public void setOptionA(String o) { this.optionA = o; }
    public String getOptionB() { return optionB; }
    public void setOptionB(String o) { this.optionB = o; }
    public String getOptionC() { return optionC; }
    public void setOptionC(String o) { this.optionC = o; }
    public String getOptionD() { return optionD; }
    public void setOptionD(String o) { this.optionD = o; }
    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String c) { this.correctAnswer = c; }
    public Integer getMarks() { return marks; }
    public void setMarks(Integer m) { this.marks = m; }
    public Integer getQuestionOrder() { return questionOrder; }
    public void setQuestionOrder(Integer o) { this.questionOrder = o; }
    public List<StudentAnswer> getStudentAnswers() { return studentAnswers; }
    public void setStudentAnswers(List<StudentAnswer> s) { this.studentAnswers = s; }
}
