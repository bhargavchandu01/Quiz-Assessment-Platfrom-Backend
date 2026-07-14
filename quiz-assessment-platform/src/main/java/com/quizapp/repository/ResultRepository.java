package com.quizapp.repository;

import com.quizapp.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByUserId(Long userId);
    List<Result> findByQuizId(Long quizId);
    Optional<Result> findByUserIdAndQuizId(Long userId, Long quizId);
    boolean existsByUserIdAndQuizId(Long userId, Long quizId);

    @Query("SELECT AVG(r.percentage) FROM Result r WHERE r.quiz.id = :quizId")
    Double findAverageScoreByQuizId(@Param("quizId") Long quizId);

    @Query("SELECT COUNT(r) FROM Result r WHERE r.quiz.id = :quizId AND r.status = 'PASS'")
    Long countPassByQuizId(@Param("quizId") Long quizId);
}
