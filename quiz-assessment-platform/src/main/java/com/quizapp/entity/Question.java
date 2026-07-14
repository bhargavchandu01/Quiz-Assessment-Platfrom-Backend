package com.quizapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @NotBlank
    @Column(nullable = false)
    private String optionA;

    @NotBlank
    @Column(nullable = false)
    private String optionB;

    @NotBlank
    @Column(nullable = false)
    private String optionC;

    @NotBlank
    @Column(nullable = false)
    private String optionD;

    @NotBlank
    @Column(nullable = false)
    private String correctAnswer;   // "A", "B", "C", or "D" (always stored uppercase)

    private String explanation;

    @Min(1)
    @Column(nullable = false)
    @Builder.Default
    private int marks = 1;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler",
            "quizzes", "questions"})
    private Category category;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
