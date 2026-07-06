package com.quizplatform.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quizzes")
public class Quiz {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "time_limit_minutes")
    private Integer timeLimitMinutes;

    @Column(name = "passing_score")
    private Integer passingScore;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Result> results = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }

    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }

    public int getTotalMarks() {
        return questions.stream().mapToInt(Question::getMarks).sum();
    }

    // Builder
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Quiz q = new Quiz();
        public Builder title(String v) { q.title = v; return this; }
        public Builder description(String v) { q.description = v; return this; }
        public Builder timeLimitMinutes(Integer v) { q.timeLimitMinutes = v; return this; }
        public Builder passingScore(Integer v) { q.passingScore = v; return this; }
        public Builder isActive(Boolean v) { q.isActive = v; return this; }
        public Builder createdBy(User v) { q.createdBy = v; return this; }
        public Quiz build() { return q; }
    }

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
    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User u) { this.createdBy = u; }
    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> q) { this.questions = q; }
    public List<Result> getResults() { return results; }
    public void setResults(List<Result> r) { this.results = r; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime c) { this.createdAt = c; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime u) { this.updatedAt = u; }
}
