package com.quizapp.repository;

import com.quizapp.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByCategoryId(Long categoryId);
    List<Question> findByCategoryIdAndActiveTrue(Long categoryId);
    long countByCategoryId(Long categoryId);
}
