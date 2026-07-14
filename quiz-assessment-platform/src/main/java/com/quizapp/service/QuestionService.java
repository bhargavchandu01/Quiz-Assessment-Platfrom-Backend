package com.quizapp.service;

import com.quizapp.dto.QuestionRequest;
import com.quizapp.entity.Category;
import com.quizapp.entity.Question;
import com.quizapp.exception.ResourceNotFoundException;
import com.quizapp.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final CategoryService categoryService;

    @Transactional
    public Question createQuestion(QuestionRequest request) {
        Category category = categoryService.getCategoryById(request.getCategoryId());
        Question question = Question.builder()
                .questionText(request.getQuestionText())
                .optionA(request.getOptionA())
                .optionB(request.getOptionB())
                .optionC(request.getOptionC())
                .optionD(request.getOptionD())
                .correctAnswer(request.getCorrectAnswer().toUpperCase())
                .explanation(request.getExplanation())
                .marks(request.getMarks())
                .category(category)
                .active(true)
                .build();
        return questionRepository.save(question);
    }

    @Transactional(readOnly = true)
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Question> getQuestionsByCategory(Long categoryId) {
        categoryService.getCategoryById(categoryId); // validates existence
        return questionRepository.findByCategoryId(categoryId);
    }

    @Transactional(readOnly = true)
    public Question getQuestionById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Question not found with id: " + id));
    }

    @Transactional
    public Question updateQuestion(Long id, QuestionRequest request) {
        Question question = getQuestionById(id);
        Category category = categoryService.getCategoryById(request.getCategoryId());
        question.setQuestionText(request.getQuestionText());
        question.setOptionA(request.getOptionA());
        question.setOptionB(request.getOptionB());
        question.setOptionC(request.getOptionC());
        question.setOptionD(request.getOptionD());
        question.setCorrectAnswer(request.getCorrectAnswer().toUpperCase());
        question.setExplanation(request.getExplanation());
        question.setMarks(request.getMarks());
        question.setCategory(category);
        return questionRepository.save(question);
    }

    @Transactional
    public void deleteQuestion(Long id) {
        questionRepository.delete(getQuestionById(id));
    }
}
