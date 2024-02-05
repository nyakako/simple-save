package com.nyakako.simplesave.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authz) -> authz
                    .requestMatchers("/register", "/login").permitAll()
                    .anyRequest().authenticated())
            .formLogin(form -> form
                    .loginPage("/login") // ログインページのパス
                    .defaultSuccessUrl("/", true) // ログイン成功時のリダイレクト先
                    .permitAll())
            .logout(logout -> logout
                    .logoutSuccessUrl("/login?logout") // ログアウト成功時のリダイレクト先
                    .permitAll());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
