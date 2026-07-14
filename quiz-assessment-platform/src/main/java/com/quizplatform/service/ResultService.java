package com.quizplatform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quizplatform.dto.QuizSubmissionDTO;
import com.quizplatform.dto.ResultDTO;
import com.quizplatform.entity.*;
import com.quizplatform.exception.ResourceNotFoundException;
import com.quizplatform.repository.AnswerRepository;
import com.quizplatform.repository.QuestionRepository;
import com.quizplatform.repository.ResultRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service

public class ResultService {

    @org.springframework.beans.factory.annotation.Autowired
    public ResultService(ResultRepository resultRepository, QuestionRepository questionRepository, AnswerRepository answerRepository, QuizService quizService, AuthService authService) {
        this.resultRepository = resultRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.quizService = quizService;
        this.authService = authService;
    }
    private static final Logger log = LoggerFactory.getLogger(ResultService.class);


    private ResultRepository resultRepository;
    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;
    private QuizService quizService;
    private AuthService authService;

    // ── Start Quiz (creates in-progress result) ────────────────────────────────
    @Transactional
    public ResultDTO startQuiz(Long quizId) {
        User student = authService.getCurrentUser();
        Quiz quiz = quizService.findQuizById(quizId);

        if (!quiz.getIsActive()) {
            throw new IllegalStateException("This quiz is not currently active");
        }

        // Check for existing in-progress attempt
        boolean hasInProgress = resultRepository.existsByStudentAndQuizAndStatus(
                student, quiz, Result.Status.IN_PROGRESS);
        if (hasInProgress) {
            throw new IllegalStateException("You already have an in-progress attempt for this quiz");
        }

        Result result = Result.builder()
                .student(student)
                .quiz(quiz)
                .status(Result.Status.IN_PROGRESS)
                .startedAt(LocalDateTime.now())
                .build();

        Result saved = resultRepository.save(result);
        log.info("Quiz started: quizId={}, studentId={}, resultId={}",
                quizId, student.getId(), saved.getId());
        return toDTO(saved, false);
    }

    // ── Submit Quiz ────────────────────────────────────────────────────────────
    @Transactional
    public ResultDTO submitQuiz(QuizSubmissionDTO submission) {
        User student = authService.getCurrentUser();

        Result result = resultRepository.findById(submission.getResultId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Result", "id", submission.getResultId()));

        if (!result.getStudent().getId().equals(student.getId())) {
            throw new AccessDeniedException("This result does not belong to you");
        }
        if (result.getStatus() != Result.Status.IN_PROGRESS) {
            throw new IllegalStateException("This quiz has already been submitted");
        }

        Quiz quiz = result.getQuiz();
        List<Question> questions = questionRepository
                .findByQuizIdOrderByQuestionOrderAsc(quiz.getId());

        Map<Long, String> answers = submission.getAnswers() != null
                ? submission.getAnswers()
                : Map.of();

        // Grade each question
        List<StudentAnswer> studentAnswers = new ArrayList<>();
        int scoreObtained = 0;
        int totalMarks = 0;

        for (Question q : questions) {
            totalMarks += q.getMarks();
            String selected = answers.get(q.getId());
            boolean correct = selected != null &&
                              selected.equalsIgnoreCase(q.getCorrectAnswer());
            int marksObtained = correct ? q.getMarks() : 0;
            scoreObtained += marksObtained;

            StudentAnswer sa = StudentAnswer.builder()
                    .result(result)
                    .question(q)
                    .selectedAnswer(selected)
                    .isCorrect(correct)
                    .marksObtained(marksObtained)
                    .build();
            studentAnswers.add(sa);
        }

        // Save answers, then add into the managed collection
        // NEVER call result.setStudentAnswers() — it breaks Hibernate orphanRemoval
        answerRepository.saveAll(studentAnswers);
        result.getStudentAnswers().addAll(studentAnswers);

        // Update result
        double percentage = totalMarks > 0
                ? ((double) scoreObtained / totalMarks) * 100 : 0;
        boolean passed = percentage >= quiz.getPassingScore();

        result.setScoreObtained(scoreObtained);
        result.setTotalMarks(totalMarks);
        result.setPercentage(Math.round(percentage * 100.0) / 100.0);
        result.setPassed(passed);
        result.setTimeTakenSeconds(submission.getTimeTakenSeconds());
        result.setStatus(Result.Status.SUBMITTED);
        result.setSubmittedAt(LocalDateTime.now());

        Result saved = resultRepository.save(result);
        log.info("Quiz submitted: resultId={}, score={}/{}, passed={}",
                saved.getId(), scoreObtained, totalMarks, passed);

        return toDTO(saved, true);
    }

    // ── My Results (student) ───────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<ResultDTO> getMyResults() {
        User student = authService.getCurrentUser();
        return resultRepository.findByStudentOrderByStartedAtDesc(student)
                .stream()
                .map(r -> toDTO(r, false))
                .collect(Collectors.toList());
    }

    // ── Result Detail ──────────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public ResultDTO getResultById(Long id) {
        User current = authService.getCurrentUser();
        Result result = resultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Result", "id", id));

        // Students can only view their own results
        if (current.getRole() == User.Role.STUDENT &&
            !result.getStudent().getId().equals(current.getId())) {
            throw new AccessDeniedException("You do not have access to this result");
        }

        return toDTO(result, true);
    }

    // ── Results for a Quiz (Teacher/Admin) ─────────────────────────────────────
    @Transactional(readOnly = true)
    public List<ResultDTO> getResultsByQuiz(Long quizId) {
        Quiz quiz = quizService.findQuizById(quizId);
        return resultRepository.findByQuizOrderBySubmittedAtDesc(quiz)
                .stream()
                .map(r -> toDTO(r, false))
                .collect(Collectors.toList());
    }

    // ── Leaderboard ────────────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<ResultDTO> getLeaderboard(Long quizId) {
        quizService.findQuizById(quizId);
        return resultRepository.findLeaderboardByQuizId(quizId)
                .stream()
                .map(r -> toDTO(r, false))
                .collect(Collectors.toList());
    }

    // ── Mapper ─────────────────────────────────────────────────────────────────
    private ResultDTO toDTO(Result result, boolean includeAnswerDetails) {
        ResultDTO dto = ResultDTO.builder()
                .id(result.getId())
                .studentId(result.getStudent().getId())
                .studentUsername(result.getStudent().getUsername())
                .quizId(result.getQuiz().getId())
                .quizTitle(result.getQuiz().getTitle())
                .scoreObtained(result.getScoreObtained())
                .totalMarks(result.getTotalMarks())
                .percentage(result.getPercentage())
                .passed(result.getPassed())
                .timeTakenSeconds(result.getTimeTakenSeconds())
                .status(result.getStatus() != null ? result.getStatus().name() : null)
                .startedAt(result.getStartedAt())
                .submittedAt(result.getSubmittedAt())
                .build();

        if (includeAnswerDetails && result.getStudentAnswers() != null) {
            List<ResultDTO.AnswerDetailDTO> details = result.getStudentAnswers()
                    .stream()
                    .map(sa -> ResultDTO.AnswerDetailDTO.builder()
                            .questionId(sa.getQuestion().getId())
                            .questionText(sa.getQuestion().getQuestionText())
                            .selectedAnswer(sa.getSelectedAnswer())
                            .correctAnswer(sa.getQuestion().getCorrectAnswer())
                            .isCorrect(sa.getIsCorrect())
                            .marks(sa.getQuestion().getMarks())
                            .marksObtained(sa.getMarksObtained())
                            .build())
                    .collect(Collectors.toList());
            dto.setAnswerDetails(details);
        }

        return dto;
    }
}
