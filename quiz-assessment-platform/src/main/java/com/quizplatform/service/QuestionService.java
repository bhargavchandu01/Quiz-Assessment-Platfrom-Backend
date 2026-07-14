package com.quizplatform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quizplatform.dto.QuestionDTO;
import com.quizplatform.entity.Question;
import com.quizplatform.entity.Quiz;
import com.quizplatform.entity.User;
import com.quizplatform.exception.ResourceNotFoundException;
import com.quizplatform.repository.QuestionRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class QuestionService {

    @org.springframework.beans.factory.annotation.Autowired
    public QuestionService(QuestionRepository questionRepository, QuizService quizService, AuthService authService) {
        this.questionRepository = questionRepository;
        this.quizService = quizService;
        this.authService = authService;
    }
    private static final Logger log = LoggerFactory.getLogger(QuestionService.class);


    private QuestionRepository questionRepository;
    private QuizService quizService;
    private AuthService authService;

    // ── Get Questions by Quiz ──────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<QuestionDTO> getQuestionsByQuiz(Long quizId) {
        quizService.findQuizById(quizId); // validate quiz exists
        User current = authService.getCurrentUser();
        boolean isStudent = current.getRole() == User.Role.STUDENT;

        return questionRepository.findByQuizIdOrderByQuestionOrderAsc(quizId)
                .stream()
                .map(q -> toDTO(q, isStudent))
                .collect(Collectors.toList());
    }

    // ── Get Single Question ────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public QuestionDTO getQuestionById(Long id) {
        Question question = findById(id);
        User current = authService.getCurrentUser();
        return toDTO(question, current.getRole() == User.Role.STUDENT);
    }

    // ── Add Question to Quiz ───────────────────────────────────────────────────
    @Transactional
    public QuestionDTO addQuestion(Long quizId, QuestionDTO dto) {
        Quiz quiz = quizService.findQuizById(quizId);
        assertOwnershipOrAdmin(quiz);

        long existingCount = questionRepository.countByQuizId(quizId);

        Question question = Question.builder()
                .quiz(quiz)
                .questionText(dto.getQuestionText())
                .optionA(dto.getOptionA())
                .optionB(dto.getOptionB())
                .optionC(dto.getOptionC())
                .optionD(dto.getOptionD())
                .correctAnswer(dto.getCorrectAnswer().toUpperCase())
                .marks(dto.getMarks() != null ? dto.getMarks() : 5)
                .questionOrder((int) existingCount + 1)
                .build();

        Question saved = questionRepository.save(question);
        log.info("Question added to quiz id={}, question id={}", quizId, saved.getId());
        return toDTO(saved, false);
    }

    // ── Update Question ────────────────────────────────────────────────────────
    @Transactional
    public QuestionDTO updateQuestion(Long id, QuestionDTO dto) {
        Question question = findById(id);
        assertOwnershipOrAdmin(question.getQuiz());

        if (dto.getQuestionText() != null)  question.setQuestionText(dto.getQuestionText());
        if (dto.getOptionA() != null)       question.setOptionA(dto.getOptionA());
        if (dto.getOptionB() != null)       question.setOptionB(dto.getOptionB());
        if (dto.getOptionC() != null)       question.setOptionC(dto.getOptionC());
        if (dto.getOptionD() != null)       question.setOptionD(dto.getOptionD());
        if (dto.getCorrectAnswer() != null) question.setCorrectAnswer(dto.getCorrectAnswer().toUpperCase());
        if (dto.getMarks() != null)         question.setMarks(dto.getMarks());
        if (dto.getQuestionOrder() != null) question.setQuestionOrder(dto.getQuestionOrder());

        return toDTO(questionRepository.save(question), false);
    }

    // ── Delete Question ────────────────────────────────────────────────────────
    @Transactional
    public void deleteQuestion(Long id) {
        Question question = findById(id);
        assertOwnershipOrAdmin(question.getQuiz());
        questionRepository.delete(question);
        log.info("Question deleted: id={}", id);
    }

    // ── Bulk Add ───────────────────────────────────────────────────────────────
    @Transactional
    public List<QuestionDTO> bulkAddQuestions(Long quizId, List<QuestionDTO> dtos) {
        return dtos.stream()
                .map(dto -> addQuestion(quizId, dto))
                .collect(Collectors.toList());
    }

    // ── Helpers ────────────────────────────────────────────────────────────────
    public Question findById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", id));
    }

    private void assertOwnershipOrAdmin(Quiz quiz) {
        User current = authService.getCurrentUser();
        if (current.getRole() == User.Role.ADMIN) return;
        if (!quiz.getCreatedBy().getId().equals(current.getId())) {
            throw new AccessDeniedException("You are not the owner of this quiz");
        }
    }

    public QuestionDTO toDTO(Question q, boolean hideAnswer) {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(q.getId());
        dto.setQuizId(q.getQuiz().getId());
        dto.setQuestionText(q.getQuestionText());
        dto.setOptionA(q.getOptionA());
        dto.setOptionB(q.getOptionB());
        dto.setOptionC(q.getOptionC());
        dto.setOptionD(q.getOptionD());
        dto.setMarks(q.getMarks());
        dto.setQuestionOrder(q.getQuestionOrder());
        if (!hideAnswer) {
            dto.setCorrectAnswer(q.getCorrectAnswer());
        }
        return dto;
    }
}
