package com.quizplatform.repository;

import com.quizplatform.entity.Result;
import com.quizplatform.entity.User;
import com.quizplatform.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

    List<Result> findByStudentOrderByStartedAtDesc(User student);

    List<Result> findByQuizOrderBySubmittedAtDesc(Quiz quiz);

    List<Result> findByStudentAndQuiz(User student, Quiz quiz);

    Optional<Result> findByIdAndStudent(Long id, User student);

    @Query("SELECT COUNT(r) FROM Result r WHERE r.student.id = :studentId AND r.status = 'SUBMITTED'")
    long countSubmittedByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT AVG(r.percentage) FROM Result r WHERE r.student.id = :studentId AND r.status = 'SUBMITTED'")
    Double avgPercentageByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT r FROM Result r WHERE r.quiz.id = :quizId AND r.status = 'SUBMITTED' ORDER BY r.percentage DESC")
    List<Result> findLeaderboardByQuizId(@Param("quizId") Long quizId);

    boolean existsByStudentAndQuizAndStatus(User student, Quiz quiz, Result.Status status);
}
