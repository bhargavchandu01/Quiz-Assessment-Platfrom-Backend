package com.quizapp.service;

import com.quizapp.dto.QuizAttemptRequest;
import com.quizapp.entity.Question;
import com.quizapp.entity.Quiz;
import com.quizapp.entity.Result;
import com.quizapp.entity.User;
import com.quizapp.exception.BadRequestException;
import com.quizapp.exception.ResourceNotFoundException;
import com.quizapp.repository.ResultRepository;
import com.quizapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResultService {

    private final ResultRepository resultRepository;
    private final QuizService quizService;
    private final UserRepository userRepository;

    @Transactional
    public Result attemptQuiz(QuizAttemptRequest request) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Authenticated user not found: " + email));

        Quiz quiz = quizService.getQuizById(request.getQuizId());

        if (!quiz.isActive()) {
            throw new BadRequestException("This quiz is not currently active");
        }

        if (resultRepository.existsByUserIdAndQuizId(user.getId(), quiz.getId())) {
            throw new BadRequestException("You have already attempted this quiz");
        }

        List<Question> questions = quiz.getQuestions();
        if (questions == null || questions.isEmpty()) {
            throw new BadRequestException("This quiz has no questions assigned yet");
        }

        Map<Long, String> answers = request.getAnswers();
        int correctCount = 0;
        int score = 0;

        for (Question q : questions) {
            String selected = answers.get(q.getId());
            if (selected != null
                    && selected.trim().equalsIgnoreCase(q.getCorrectAnswer())) {
                correctCount++;
                score += q.getMarks();
            }
        }

        long answeredCount = answers.values().stream()
                .filter(a -> a != null && !a.isBlank())
                .count();
        int wrongCount = Math.max(0, (int) answeredCount - correctCount);

        double percentage = quiz.getMaxMarks() > 0
                ? Math.round(((double) score / quiz.getMaxMarks()) * 10000.0) / 100.0
                : 0.0;

        Result.ResultStatus status = percentage >= 40.0
                ? Result.ResultStatus.PASS
                : Result.ResultStatus.FAIL;

        Result result = Result.builder()
                .user(user)
                .quiz(quiz)
                .score(score)
                .maxMarks(quiz.getMaxMarks())
                .totalQuestions(questions.size())
                .correctAnswers(correctCount)
                .wrongAnswers(wrongCount)
                .percentage(percentage)
                .status(status)
                .attemptedAnswers(answers)
                .build();

        return resultRepository.save(result);
    }

    @Transactional(readOnly = true)
    public List<Result> getMyResults() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Authenticated user not found: " + email));
        return resultRepository.findByUserId(user.getId());
    }

    @Transactional(readOnly = true)
    public List<Result> getResultsByUserId(Long userId) {
        return resultRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Result> getResultsByQuizId(Long quizId) {
        quizService.getQuizById(quizId); // validates existence
        return resultRepository.findByQuizId(quizId);
    }

    @Transactional(readOnly = true)
    public Result getResultById(Long id) {
        return resultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Result not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Double getAverageScoreForQuiz(Long quizId) {
        quizService.getQuizById(quizId);
        return resultRepository.findAverageScoreByQuizId(quizId);
    }
}
