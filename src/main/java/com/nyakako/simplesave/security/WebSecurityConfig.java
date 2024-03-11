package com.nyakako.simplesave.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

        private final CustomUserDetailsService userDetailsService;

        public WebSecurityConfig(CustomUserDetailsService userDetailsService) {
                this.userDetailsService = userDetailsService;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .userDetailsService(userDetailsService)
                                // Permit access to H2 console, login, and register pages
                                .authorizeHttpRequests(authz -> authz
                                                .requestMatchers("/guestLogin", "/register", "/login", "/css/**",
                                                                "/js/**")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                // Configure form login
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                .failureHandler(customAuthenticationFailureHandler())
                                                .defaultSuccessUrl("/dashboard", true)
                                                .usernameParameter("email")
                                                .permitAll())
                                // Configure logout
                                .logout(logout -> logout
                                                .logoutUrl("/logout") // ログアウトを実行するURL
                                                .logoutSuccessUrl("/login?logout") // ログアウト成功後にリダイレクトするURL
                                                .invalidateHttpSession(true) // HTTPセッションを無効化する
                                                .deleteCookies("JSESSIONID") // JSESSIONIDクッキーを削除する
                                                .permitAll())
                                // Disable CSRF protection for the H2 console
                                .csrf(csrf -> csrf
                                                .ignoringRequestMatchers("/h2-console/**"))
                                // Allow frames for H2 console
                                .headers(headers -> headers.frameOptions(
                                                frame -> frame.sameOrigin()))
                                .exceptionHandling(eh -> eh.accessDeniedPage("/403error"));

                return http.build();
        }

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationFailureHandler customAuthenticationFailureHandler() {
                return new CustomAuthenticationFailureHandler();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }
}
