package com.quizplatform.dto;

import java.time.LocalDateTime;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String role;
    private LocalDateTime createdAt;
    private int totalQuizzesTaken;
    private int totalQuizzesCreated;

    public UserDTO() {}
    public UserDTO(Long id, String username, String email, String role, LocalDateTime createdAt, int taken, int created) {
        this.id = id; this.username = username; this.email = email;
        this.role = role; this.createdAt = createdAt;
        this.totalQuizzesTaken = taken; this.totalQuizzesCreated = created;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String u) { this.username = u; }
    public String getEmail() { return email; }
    public void setEmail(String e) { this.email = e; }
    public String getRole() { return role; }
    public void setRole(String r) { this.role = r; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime c) { this.createdAt = c; }
    public int getTotalQuizzesTaken() { return totalQuizzesTaken; }
    public void setTotalQuizzesTaken(int t) { this.totalQuizzesTaken = t; }
    public int getTotalQuizzesCreated() { return totalQuizzesCreated; }
    public void setTotalQuizzesCreated(int c) { this.totalQuizzesCreated = c; }

    // Builder pattern used by AuthService
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private String username, email, role;
        private LocalDateTime createdAt; private int taken, created;
        public Builder id(Long id) { this.id = id; return this; }
        public Builder username(String u) { this.username = u; return this; }
        public Builder email(String e) { this.email = e; return this; }
        public Builder role(String r) { this.role = r; return this; }
        public Builder createdAt(LocalDateTime c) { this.createdAt = c; return this; }
        public Builder totalQuizzesTaken(int t) { this.taken = t; return this; }
        public Builder totalQuizzesCreated(int c) { this.created = c; return this; }
        public UserDTO build() { return new UserDTO(id, username, email, role, createdAt, taken, created); }
    }
}
