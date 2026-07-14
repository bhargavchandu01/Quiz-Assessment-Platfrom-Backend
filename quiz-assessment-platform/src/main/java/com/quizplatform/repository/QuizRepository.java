package com.quizplatform.repository;

import com.quizplatform.entity.Quiz;
import com.quizplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    List<Quiz> findByIsActiveTrue();

    List<Quiz> findByCreatedBy(User user);

    List<Quiz> findByCreatedByAndIsActiveTrue(User user);

    @Query("SELECT q FROM Quiz q WHERE q.isActive = true AND " +
           "(LOWER(q.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(q.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Quiz> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT COUNT(q) FROM Quiz q WHERE q.createdBy.id = :userId")
    long countByCreatedById(@Param("userId") Long userId);
}
