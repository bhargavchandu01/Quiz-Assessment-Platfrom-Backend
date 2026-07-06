package com.quizplatform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quizplatform.dto.QuizDTO;
import com.quizplatform.entity.Quiz;
import com.quizplatform.entity.User;
import com.quizplatform.exception.ResourceNotFoundException;
import com.quizplatform.repository.QuizRepository;
import com.quizplatform.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class QuizService {

    @org.springframework.beans.factory.annotation.Autowired
    public QuizService(QuizRepository quizRepository, QuestionRepository questionRepository, AuthService authService) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.authService = authService;
    }
    private static final Logger log = LoggerFactory.getLogger(QuizService.class);


    private QuizRepository quizRepository;
    private QuestionRepository questionRepository;
    private AuthService authService;

    // ── Get All Active Quizzes ─────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<QuizDTO> getAllActiveQuizzes() {
        return quizRepository.findByIsActiveTrue()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ── Get All Quizzes (Admin/Teacher) ────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<QuizDTO> getAllQuizzes() {
        return quizRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ── Get Quiz by ID ─────────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public QuizDTO getQuizById(Long id) {
        Quiz quiz = findQuizById(id);
        QuizDTO dto = toDTO(quiz);

        // Load questions
        dto.setQuestions(
            questionRepository.findByQuizIdOrderByQuestionOrderAsc(id)
                .stream()
                .map(q -> {
                    com.quizplatform.dto.QuestionDTO qDTO = new com.quizplatform.dto.QuestionDTO();
                    qDTO.setId(q.getId());
                    qDTO.setQuizId(id);
                    qDTO.setQuestionText(q.getQuestionText());
                    qDTO.setOptionA(q.getOptionA());
                    qDTO.setOptionB(q.getOptionB());
                    qDTO.setOptionC(q.getOptionC());
                    qDTO.setOptionD(q.getOptionD());
                    qDTO.setMarks(q.getMarks());
                    qDTO.setQuestionOrder(q.getQuestionOrder());
                    // Correct answer hidden from students
                    User current = authService.getCurrentUser();
                    if (current.getRole() != User.Role.STUDENT) {
                        qDTO.setCorrectAnswer(q.getCorrectAnswer());
                    }
                    return qDTO;
                })
                .collect(Collectors.toList())
        );
        return dto;
    }

    // ── Create Quiz ────────────────────────────────────────────────────────────
    @Transactional
    public QuizDTO createQuiz(QuizDTO dto) {
        User currentUser = authService.getCurrentUser();

        Quiz quiz = Quiz.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .timeLimitMinutes(dto.getTimeLimitMinutes())
                .passingScore(dto.getPassingScore() != null ? dto.getPassingScore() : 60)
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .createdBy(currentUser)
                .build();

        Quiz saved = quizRepository.save(quiz);
        log.info("Quiz created: '{}' by user: {}", saved.getTitle(), currentUser.getUsername());
        return toDTO(saved);
    }

    // ── Update Quiz ────────────────────────────────────────────────────────────
    @Transactional
    public QuizDTO updateQuiz(Long id, QuizDTO dto) {
        Quiz quiz = findQuizById(id);
        assertOwnershipOrAdmin(quiz);

        if (dto.getTitle() != null)          quiz.setTitle(dto.getTitle());
        if (dto.getDescription() != null)    quiz.setDescription(dto.getDescription());
        if (dto.getTimeLimitMinutes() != null) quiz.setTimeLimitMinutes(dto.getTimeLimitMinutes());
        if (dto.getPassingScore() != null)   quiz.setPassingScore(dto.getPassingScore());
        if (dto.getIsActive() != null)       quiz.setIsActive(dto.getIsActive());

        Quiz updated = quizRepository.save(quiz);
        log.info("Quiz updated: id={}", id);
        return toDTO(updated);
    }

    // ── Delete Quiz ────────────────────────────────────────────────────────────
    @Transactional
    public void deleteQuiz(Long id) {
        Quiz quiz = findQuizById(id);
        assertOwnershipOrAdmin(quiz);
        quizRepository.delete(quiz);
        log.info("Quiz deleted: id={}", id);
    }

    // ── Toggle Active ──────────────────────────────────────────────────────────
    @Transactional
    public QuizDTO toggleActive(Long id) {
        Quiz quiz = findQuizById(id);
        assertOwnershipOrAdmin(quiz);
        quiz.setIsActive(!quiz.getIsActive());
        return toDTO(quizRepository.save(quiz));
    }

    // ── Search ─────────────────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<QuizDTO> searchQuizzes(String keyword) {
        return quizRepository.searchByKeyword(keyword)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ── My Quizzes (Teacher) ───────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<QuizDTO> getMyQuizzes() {
        User current = authService.getCurrentUser();
        return quizRepository.findByCreatedBy(current)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ── Helpers ────────────────────────────────────────────────────────────────
    public Quiz findQuizById(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));
    }

    private void assertOwnershipOrAdmin(Quiz quiz) {
        User current = authService.getCurrentUser();
        if (current.getRole() == User.Role.ADMIN) return;
        if (!quiz.getCreatedBy().getId().equals(current.getId())) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "You are not the owner of this quiz");
        }
    }

    public QuizDTO toDTO(Quiz quiz) {
        QuizDTO dto = new QuizDTO();
        dto.setId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        dto.setDescription(quiz.getDescription());
        dto.setTimeLimitMinutes(quiz.getTimeLimitMinutes());
        dto.setPassingScore(quiz.getPassingScore());
        dto.setIsActive(quiz.getIsActive());
        dto.setCreatedById(quiz.getCreatedBy().getId());
        dto.setCreatedByUsername(quiz.getCreatedBy().getUsername());
        dto.setCreatedAt(quiz.getCreatedAt());
        dto.setUpdatedAt(quiz.getUpdatedAt());

        long qCount = questionRepository.countByQuizId(quiz.getId());
        dto.setQuestionCount((int) qCount);

        Integer total = questionRepository.sumMarksByQuizId(quiz.getId());
        dto.setTotalMarks(total != null ? total : 0);

        return dto;
    }
}
