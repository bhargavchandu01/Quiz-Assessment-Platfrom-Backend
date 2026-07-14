package com.quizapp.repository;

import com.quizapp.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByActiveTrue();
    List<Quiz> findByCategoryIdAndActiveTrue(Long categoryId);
    List<Quiz> findByCategoryId(Long categoryId);
}
