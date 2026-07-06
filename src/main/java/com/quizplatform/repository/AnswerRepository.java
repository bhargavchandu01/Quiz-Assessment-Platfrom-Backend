package com.quizplatform.repository;

import com.quizplatform.entity.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<StudentAnswer, Long> {

    List<StudentAnswer> findByResultId(Long resultId);

    List<StudentAnswer> findByResultIdAndIsCorrect(Long resultId, Boolean isCorrect);

    @Query("SELECT COUNT(sa) FROM StudentAnswer sa WHERE sa.result.id = :resultId AND sa.isCorrect = true")
    long countCorrectByResultId(@Param("resultId") Long resultId);

    @Query("SELECT SUM(sa.marksObtained) FROM StudentAnswer sa WHERE sa.result.id = :resultId")
    Integer sumMarksObtainedByResultId(@Param("resultId") Long resultId);

    void deleteByResultId(Long resultId);
}
