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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Permit access to H2 console, login, and register pages
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/h2-console/**", "/register", "/login").permitAll()
                        .anyRequest().authenticated())
                // Configure form login
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/transactions", true)
                        .permitAll())
                // Configure logout
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll())
                // Disable CSRF protection for the H2 console
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**"))
                // Allow frames for H2 console
                .headers(headers -> headers.frameOptions(
                        frame -> frame.sameOrigin()));

        return http.build();
    }

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
    // Exception {
    // http.securityMatcher(AntPathRequestMatcher.antMatcher("/h2-console/**"))
    // .authorizeHttpRequests(authz -> authz
    // .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll())
    // .csrf(csrf -> csrf
    // .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")))
    // .headers(headers -> headers.frameOptions(
    // frame -> frame.sameOrigin()))
    // .securityMatcher("/**")
    // .formLogin(Customizer.withDefaults())

    // .authorizeHttpRequests(authz -> authz.anyRequest().authenticated());
    // return http.build();
    // }

    // .authorizeHttpRequests((requests) -> requests
    // .requestMatchers("/register", "/login").permitAll()
    // .anyRequest().authenticated())
    // .formLogin(form -> form
    // .loginPage("/login")
    // .defaultSuccessUrl("/", true)
    // .permitAll())
    // .logout(logout -> logout
    // .logoutSuccessUrl("/login?logout")
    // .permitAll())
    // .csrf(Customizer.withDefaults())
    // .headers(Customizer.withDefaults())

    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // http
    // .authorizeHttpRequests((requests) -> requests
    // .requestMatchers("/register", "/login").permitAll()
    // .anyRequest().authenticated())
    // .formLogin(form -> form
    // .loginPage("/login")
    // .defaultSuccessUrl("/", true)
    // .permitAll())
    // .logout(logout -> logout
    // .logoutSuccessUrl("/login?logout")
    // .permitAll());

    // return http.build();
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
