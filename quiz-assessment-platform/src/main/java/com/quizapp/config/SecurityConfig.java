package com.quizapp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security configuration — Spring Boot 4.0.3 / Spring Security 7.
 *
 * ═══════════════════════════════════════════════════════════
 * CIRCULAR DEPENDENCY: ROOT CAUSE AND SOLUTION
 * ═══════════════════════════════════════════════════════════
 *
 * ROOT CAUSE:
 *   SecurityConfig (constructor) injects JwtFilter
 *   JwtFilter      (constructor) injects UserDetailsService
 *   UserDetailsService is UserService (@Service)
 *   Spring Security also needs UserService to build AuthenticationManager
 *   → Spring detects: SecurityConfig → JwtFilter → UserService ← SecurityConfig
 *
 * SOLUTION — two changes together eliminate the cycle:
 *
 *   1. JwtFilter uses @Lazy @Autowired for UserDetailsService (not constructor).
 *      Spring builds a CGLIB proxy placeholder for UserDetailsService,
 *      resolving the real bean only on first HTTP request. No startup cycle.
 *
 *   2. SecurityConfig does NOT constructor-inject UserDetailsService.
 *      Instead, UserDetailsService is passed as a @Bean method parameter
 *      to authenticationProvider(). Spring resolves @Bean method params
 *      from the context during the bean creation phase, after all constructors
 *      have run. Both securityFilterChain and authenticationProvider
 *      receive AuthenticationProvider/UserDetailsService as method params.
 *
 * ═══════════════════════════════════════════════════════════
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // Safe: JwtFilter constructor only takes JwtUtil (zero security-bean deps)
    private final JwtFilter jwtFilter;

    // Safe: plain bean from AppConfig, no security bootstrap involvement
    private final PasswordEncoder passwordEncoder;

    /**
     * SecurityFilterChain receives AuthenticationProvider as a @Bean method param.
     * Spring pulls the already-built authenticationProvider bean from the context.
     * No constructor-level cycle.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationProvider authenticationProvider) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // ── Public ────────────────────────────────────────────────────
                .requestMatchers("/api/auth/**").permitAll()
                // ── Admin-only write operations ───────────────────────────────
                .requestMatchers(HttpMethod.POST,   "/api/categories/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/api/categories/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH,  "/api/categories/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,   "/api/questions/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/api/questions/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/questions/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,   "/api/quizzes/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/api/quizzes/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH,  "/api/quizzes/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/quizzes/**").hasRole("ADMIN")
                .requestMatchers("/api/users/**").hasRole("ADMIN")
                // ── All other endpoints require authentication ─────────────────
                .anyRequest().authenticated()
            )
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * UserDetailsService arrives as a @Bean method parameter.
     * Spring resolves it from the context AFTER all @Configuration constructors
     * have run — no constructor-phase cycle.
     */
    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
