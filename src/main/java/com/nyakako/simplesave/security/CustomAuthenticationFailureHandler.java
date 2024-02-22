package com.nyakako.simplesave.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // ユーザーが入力したメールアドレスをセッションに保存
        String email = request.getParameter("email");
        if (email != null) {
            request.getSession().setAttribute("email", email);
        }

        // ログインページにリダイレクトし、エラーメッセージを表示
        response.sendRedirect(request.getContextPath() + "/login?error");
    }
}
