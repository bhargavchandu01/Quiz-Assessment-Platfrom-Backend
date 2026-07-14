package com.quizapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * General application configuration.
 *
 * IMPORTANT: PasswordEncoder is defined HERE and ONLY here.
 * It is NOT declared in SecurityConfig to avoid duplicate bean conflicts.
 * SecurityConfig imports it via @RequiredArgsConstructor injection.
 *
 * Rule: SecurityConfig → only SecurityFilterChain + AuthenticationProvider + AuthenticationManager.
 *       AppConfig       → PasswordEncoder and any other shared infrastructure beans.
 */
@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
