package com.quizplatform.dto;

import jakarta.validation.constraints.*;

public class QuestionDTO {
    private Long id;
    private Long quizId;
    @NotBlank(message = "Question text is required")
    private String questionText;
    @NotBlank(message = "Option A is required")
    private String optionA;
    @NotBlank(message = "Option B is required")
    private String optionB;
    @NotBlank(message = "Option C is required")
    private String optionC;
    @NotBlank(message = "Option D is required")
    private String optionD;
    @Pattern(regexp = "^[ABCD]$", message = "Correct answer must be A, B, C, or D")
    private String correctAnswer;
    @Min(value = 1, message = "Marks must be at least 1")
    private Integer marks;
    private Integer questionOrder;

    public QuestionDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getQuizId() { return quizId; }
    public void setQuizId(Long q) { this.quizId = q; }
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
}
