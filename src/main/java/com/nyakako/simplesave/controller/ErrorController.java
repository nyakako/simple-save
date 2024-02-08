package com.nyakako.simplesave.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ErrorController
 */
@Controller
public class ErrorController {

    @GetMapping("/403error")
    public String handleAccessDenideError(Model model) {
        model.addAttribute("title", "403エラー - simplesave");
        model.addAttribute("content", "errors/403");
        return "layout";
    }

}