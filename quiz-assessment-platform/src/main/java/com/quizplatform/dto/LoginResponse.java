package com.quizplatform.dto;

public class LoginResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String role;

    public LoginResponse() {}
    public LoginResponse(String token, Long id, String username, String email, String role) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public String getToken() { return token; }
    public void setToken(String t) { this.token = t; }
    public String getType() { return type; }
    public void setType(String t) { this.type = t; }
    public Long getId() { return id; }
    public void setId(Long i) { this.id = i; }
    public String getUsername() { return username; }
    public void setUsername(String u) { this.username = u; }
    public String getEmail() { return email; }
    public void setEmail(String e) { this.email = e; }
    public String getRole() { return role; }
    public void setRole(String r) { this.role = r; }
}
