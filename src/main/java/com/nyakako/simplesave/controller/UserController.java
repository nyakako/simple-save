package com.nyakako.simplesave.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nyakako.simplesave.model.User;
import com.nyakako.simplesave.service.UserService;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        model.addAttribute("title", "新規ユーザー登録 - simplesave");
        model.addAttribute("content", "registration");
        return "layout";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Validated User user, BindingResult result, RedirectAttributes redirectAttributes) {
        // ユーザー名とメールアドレスの存在チェック
        if (userService.isUsernameExists(user.getUsername())) {
            result.rejectValue("username", "error.user", "このユーザー名は使用できません");
        }
        if (userService.isEmailExists(user.getEmail())) {
            result.rejectValue("email", "error.user", "このメールアドレスは使用できません");
        }

        if (result.hasErrors()) {
            // エラーがある場合、登録フォームに戻る
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/register";
        }

        userService.registerUser(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error, Model model) {
        if(error != null) {
            model.addAttribute("loginError", "メールアドレスまたはパスワードが無効です");
        }

        model.addAttribute("title", "ログイン - simplesave");
        model.addAttribute("content", "login");
        return "layout";
    }
}
