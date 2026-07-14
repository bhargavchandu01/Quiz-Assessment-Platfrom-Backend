package com.quizapp.service;

import com.quizapp.dto.QuizRequest;
import com.quizapp.entity.Category;
import com.quizapp.entity.Question;
import com.quizapp.entity.Quiz;
import com.quizapp.exception.BadRequestException;
import com.quizapp.exception.ResourceNotFoundException;
import com.quizapp.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final CategoryService categoryService;
    private final QuestionService questionService;

    @Transactional
    public Quiz createQuiz(QuizRequest request) {
        Category category = categoryService.getCategoryById(request.getCategoryId());

        List<Question> questions = null;
        if (request.getQuestionIds() != null && !request.getQuestionIds().isEmpty()) {
            questions = request.getQuestionIds().stream()
                    .map(questionService::getQuestionById)
                    .toList();
            if (questions.size() < request.getNumberOfQuestions()) {
                throw new BadRequestException(
                        "Not enough questions provided. Required: "
                        + request.getNumberOfQuestions()
                        + ", provided: " + questions.size());
            }
        }

        Quiz quiz = Quiz.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .numberOfQuestions(request.getNumberOfQuestions())
                .maxMarks(request.getMaxMarks())
                .timeLimitMinutes(request.getTimeLimitMinutes())
                .category(category)
                .questions(questions)
                .active(true)
                .build();

        return quizRepository.save(quiz);
    }

    @Transactional(readOnly = true)
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Quiz> getActiveQuizzes() {
        return quizRepository.findByActiveTrue();
    }

    @Transactional(readOnly = true)
    public List<Quiz> getQuizzesByCategory(Long categoryId) {
        categoryService.getCategoryById(categoryId);
        return quizRepository.findByCategoryIdAndActiveTrue(categoryId);
    }

    @Transactional(readOnly = true)
    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Quiz not found with id: " + id));
    }

    @Transactional
    public Quiz updateQuiz(Long id, QuizRequest request) {
        Quiz quiz = getQuizById(id);
        Category category = categoryService.getCategoryById(request.getCategoryId());
        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setNumberOfQuestions(request.getNumberOfQuestions());
        quiz.setMaxMarks(request.getMaxMarks());
        quiz.setTimeLimitMinutes(request.getTimeLimitMinutes());
        quiz.setCategory(category);
        if (request.getQuestionIds() != null) {
            List<Question> questions = request.getQuestionIds().stream()
                    .map(questionService::getQuestionById)
                    .toList();
            quiz.setQuestions(questions);
        }
        return quizRepository.save(quiz);
    }

    @Transactional
    public void deleteQuiz(Long id) {
        quizRepository.delete(getQuizById(id));
    }

    @Transactional
    public Quiz toggleQuizStatus(Long id) {
        Quiz quiz = getQuizById(id);
        quiz.setActive(!quiz.isActive());
        return quizRepository.save(quiz);
    }
}
