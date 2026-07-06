package com.quizplatform.repository;

import com.quizplatform.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByQuizIdOrderByQuestionOrderAsc(Long quizId);

    long countByQuizId(Long quizId);

    void deleteByQuizId(Long quizId);

    @Query("SELECT SUM(q.marks) FROM Question q WHERE q.quiz.id = :quizId")
    Integer sumMarksByQuizId(@Param("quizId") Long quizId);
}
