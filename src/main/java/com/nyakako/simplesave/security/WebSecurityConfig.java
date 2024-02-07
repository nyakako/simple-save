package com.nyakako.simplesave.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                // Permit access to H2 console, login, and register pages
                                .authorizeHttpRequests(authz -> authz
                                                .requestMatchers("/h2-console/**", "/register", "/login").permitAll()
                                                .anyRequest().authenticated())
                                // Configure form login
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                .defaultSuccessUrl("/transactions", true)
                                                .permitAll())
                                // Configure logout
                                .logout(logout -> logout
                                                .logoutUrl("/logout")  // ログアウトを実行するURL
                                                .logoutSuccessUrl("/login?logout")  // ログアウト成功後にリダイレクトするURL
                                                .invalidateHttpSession(true)  // HTTPセッションを無効化する
                                                .deleteCookies("JSESSIONID")  // JSESSIONIDクッキーを削除する
                                                .permitAll())
                                // Disable CSRF protection for the H2 console
                                .csrf(csrf -> csrf
                                                .ignoringRequestMatchers("/h2-console/**"))
                                // Allow frames for H2 console
                                .headers(headers -> headers.frameOptions(
                                                frame -> frame.sameOrigin()));

                return http.build();
        }

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}