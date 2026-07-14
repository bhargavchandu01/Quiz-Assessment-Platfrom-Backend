package com.quizplatform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quizplatform.dto.UserDTO;
import com.quizplatform.entity.User;
import com.quizplatform.exception.ResourceNotFoundException;
import com.quizplatform.repository.QuizRepository;
import com.quizplatform.repository.ResultRepository;
import com.quizplatform.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class UserService {

    @org.springframework.beans.factory.annotation.Autowired
    public UserService(UserRepository userRepository, QuizRepository quizRepository, ResultRepository resultRepository, PasswordEncoder passwordEncoder, AuthService authService) {
        this.userRepository = userRepository;
        this.quizRepository = quizRepository;
        this.resultRepository = resultRepository;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }
    private static final Logger log = LoggerFactory.getLogger(UserService.class);


    private UserRepository userRepository;
    private QuizRepository quizRepository;
    private ResultRepository resultRepository;
    private PasswordEncoder passwordEncoder;
    private AuthService authService;

    // ── Get All Users ──────────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ── Get User by ID ─────────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        return toDTO(findById(id));
    }

    // ── Get Current User Profile ───────────────────────────────────────────────
    @Transactional(readOnly = true)
    public UserDTO getMyProfile() {
        return toDTO(authService.getCurrentUser());
    }

    // ── Update Role ────────────────────────────────────────────────────────────
    @Transactional
    public UserDTO updateUserRole(Long id, String role) {
        User user = findById(id);
        try {
            user.setRole(User.Role.valueOf(role.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + role +
                    ". Valid roles: ADMIN, TEACHER, STUDENT");
        }
        log.info("Role updated for user id={} to {}", id, role);
        return toDTO(userRepository.save(user));
    }

    // ── Change Password ────────────────────────────────────────────────────────
    @Transactional
    public void changePassword(String currentPassword, String newPassword) {
        User user = authService.getCurrentUser();
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        if (newPassword.length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password changed for user: {}", user.getUsername());
    }

    // ── Delete User ────────────────────────────────────────────────────────────
    @Transactional
    public void deleteUser(Long id) {
        User user = findById(id);
        userRepository.delete(user);
        log.info("User deleted: id={}, username={}", id, user.getUsername());
    }

    // ── Get Users by Role ──────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<UserDTO> getUsersByRole(String role) {
        User.Role userRole;
        try {
            userRole = User.Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
        return userRepository.findByRole(userRole)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ── Helpers ────────────────────────────────────────────────────────────────
    private User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    private UserDTO toDTO(User user) {
        long quizzesCreated = quizRepository.countByCreatedById(user.getId());
        long quizzesTaken = resultRepository.countSubmittedByStudentId(user.getId());

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .totalQuizzesCreated((int) quizzesCreated)
                .totalQuizzesTaken((int) quizzesTaken)
                .build();
    }
}
