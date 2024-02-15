package com.nyakako.simplesave.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nyakako.simplesave.model.Category;
import com.nyakako.simplesave.model.RecurringTransaction;
import com.nyakako.simplesave.security.CustomUserDetails;
import com.nyakako.simplesave.service.CategoryService;
import com.nyakako.simplesave.service.RecurringTransactionService;
import com.nyakako.simplesave.service.UserService;

@Controller
public class SettingController {

    private final CategoryService categoryService;
    private final RecurringTransactionService recurringTransactionService;
    private final UserService userService;

    public SettingController(CategoryService categoryService, RecurringTransactionService recurringTransactionService,
            UserService userService) {
        this.categoryService = categoryService;
        this.recurringTransactionService = recurringTransactionService;
        this.userService = userService;
    }

    @GetMapping("/settings/categories-expense")
    public String showExpenseCategory(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId(); // ユーザーIDの取得
        List<Category> expenseCategories = categoryService.findCategoriesByTypeAndUserId("expense", userId);
        model.addAttribute("expenseCategories", expenseCategories);
        model.addAttribute("title", "支出カテゴリ設定 - simplesave");
        model.addAttribute("content", "categories-expense");
        return "layout";
    }

    @GetMapping("/settings/categories-income")
    public String showIncomeCategory(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId(); // ユーザーIDの取得
        List<Category> incomeCategories = categoryService.findCategoriesByTypeAndUserId("income", userId);
        model.addAttribute("incomeCategories", incomeCategories);
        model.addAttribute("title", "収入カテゴリ設定 - simplesave");
        model.addAttribute("content", "categories-income");
        return "layout";
    }

    @GetMapping("/settings/recurring-transactions")
    public String showRecurringTransactions(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId(); // ユーザーIDの取得
        List<RecurringTransaction> transactions = recurringTransactionService.findRecurringTransactionsByUserId(userId);
        model.addAttribute("transactions", transactions);
        model.addAttribute("title", "定期取引一覧 - simplesave");
        model.addAttribute("content", "recurring-transactions");
        return "layout";
    }

    @GetMapping("/settings/color-setting")
    public String showColorSetting(Model model, Authentication authentication) {
        String colorPreference = "greenPositive";
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();
        if (userId != null) {
            colorPreference = userService.getColorPreference(userId);
        }

        model.addAttribute("colorPreference", colorPreference);
        model.addAttribute("title", "色設定 - simplesave");
        model.addAttribute("content", "color-setting");
        return "layout";
    }

    @PostMapping("/color-setting/edit")
    public String editColorSetting(@RequestParam("colorSetting") String colorSetting, Model model,
            Authentication authentication, RedirectAttributes redirectAttribtes) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId(); // ユーザーIDの取得
        if (userId != null) {
            try {
                userService.updateColorPreference(userId, colorSetting);
                // model.addAttribute("successMessage", "色設定が更新されました。");
                redirectAttribtes.addFlashAttribute("successMessage", "色設定が更新されました。");
            } catch (Exception e) {
                // model.addAttribute("errorMessage", "色設定の更新に失敗しました。");
                redirectAttribtes.addFlashAttribute("errorMessage", "色設定の更新に失敗しました。");
            }
        }

        return "redirect:/settings/color-setting";
    }
}
