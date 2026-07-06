package com.quizplatform.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "results")
public class Result {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(name = "score_obtained") private Integer scoreObtained;
    @Column(name = "total_marks")    private Integer totalMarks;
    @Column(name = "percentage")     private Double percentage;
    @Column(name = "passed")         private Boolean passed;
    @Column(name = "time_taken_seconds") private Integer timeTakenSeconds;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.IN_PROGRESS;

    @Column(name = "submitted_at") private LocalDateTime submittedAt;
    @Column(name = "started_at")   private LocalDateTime startedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "result", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StudentAnswer> studentAnswers = new ArrayList<>();

    public enum Status { IN_PROGRESS, SUBMITTED, GRADED }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Result r = new Result();
        public Builder student(User v) { r.student = v; return this; }
        public Builder quiz(Quiz v) { r.quiz = v; return this; }
        public Builder status(Status v) { r.status = v; return this; }
        public Builder startedAt(LocalDateTime v) { r.startedAt = v; return this; }
        public Builder scoreObtained(Integer v) { r.scoreObtained = v; return this; }
        public Builder totalMarks(Integer v) { r.totalMarks = v; return this; }
        public Builder percentage(Double v) { r.percentage = v; return this; }
        public Builder passed(Boolean v) { r.passed = v; return this; }
        public Builder timeTakenSeconds(Integer v) { r.timeTakenSeconds = v; return this; }
        public Builder submittedAt(LocalDateTime v) { r.submittedAt = v; return this; }
        public Result build() { return r; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getStudent() { return student; }
    public void setStudent(User s) { this.student = s; }
    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz q) { this.quiz = q; }
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
    public Status getStatus() { return status; }
    public void setStatus(Status s) { this.status = s; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime s) { this.submittedAt = s; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime s) { this.startedAt = s; }
    public List<StudentAnswer> getStudentAnswers() { return studentAnswers; }
    public void setStudentAnswers(List<StudentAnswer> s) { this.studentAnswers = s; }
}
