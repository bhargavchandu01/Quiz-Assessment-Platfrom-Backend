package com.quizapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler",
            "password", "authorities", "accountNonExpired",
            "accountNonLocked", "credentialsNonExpired", "enabled"})
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler",
            "questions", "results", "category"})
    private Quiz quiz;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    private int maxMarks;

    @Column(nullable = false)
    private int totalQuestions;

    @Column(nullable = false)
    private int correctAnswers;

    @Column(nullable = false)
    private int wrongAnswers;

    private double percentage;

    @Enumerated(EnumType.STRING)
    private ResultStatus status;

    // @Builder.Default ensures Lombok initialises this to an empty map
    // instead of null when the builder is used without setting attemptedAnswers.
    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "result_answers",
        joinColumns = @JoinColumn(name = "result_id")
    )
    @MapKeyColumn(name = "question_id")
    @Column(name = "selected_answer")
    private Map<Long, String> attemptedAnswers = new HashMap<>();

    @Column(updatable = false)
    private LocalDateTime attemptedAt;

    @PrePersist
    protected void onCreate() {
        attemptedAt = LocalDateTime.now();
    }

    public enum ResultStatus {
        PASS, FAIL
    }
}
