package com.nyakako.simplesave.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.nyakako.simplesave.model.Category;
import com.nyakako.simplesave.model.RecurringTransaction;
import com.nyakako.simplesave.model.User;
import com.nyakako.simplesave.security.CustomUserDetails;
import com.nyakako.simplesave.service.CategoryService;
import com.nyakako.simplesave.service.RecurringTransactionService;
import com.nyakako.simplesave.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RecurringTransactionController {

    private final RecurringTransactionService recurringTransactionService;
    private final CategoryService categoryService;
    private final UserService userService;

    public RecurringTransactionController(RecurringTransactionService recurringTransactionService,
            CategoryService categoryService, UserService userService) {
        this.recurringTransactionService = recurringTransactionService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping("/recurring-transactions/new")
    public String newRecurringTransaction(Model model, Authentication authentication) {
        LocalDate today = LocalDate.now();
        LocalDate oneMonthLater = today.plusMonths(1);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId(); // ユーザーIDの取得
        model.addAttribute("today", today);
        model.addAttribute("oneMonthLater", oneMonthLater);
        model.addAttribute("categories", categoryService.findCategoriesByUserId(userId));
        model.addAttribute("title", "新規明細登録 - simplesave");
        model.addAttribute("content", "new-recurring-transaction");
        return "layout";
    }

    @PostMapping("/recurring-transactions/new")
    public String addRecurringTransaction(@ModelAttribute RecurringTransaction transaction,
            @NonNull @RequestParam("categoryId") Long categoryId, Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId(); // ユーザーIDの取得
        if (userId != null) {
            User user = userService.findUserById(userId).orElse(null);
            transaction.setUser(user);
        }

        // categoryIdを使用してCategoryオブジェクトを取得
        Category category = categoryService.findCategoryById(categoryId).orElse(null);

        // TransactionオブジェクトにCategoryをセット
        transaction.setCategory(category);

        // intervalUnitに基づいて登録フィールド以外をクリア
        switch (transaction.getIntervalUnit()) {
            case "months":
                transaction.setDayOfMonth(null);
                transaction.setDayOfWeek(null);
                transaction.setMonthOfYear(null);
                break;
            case "weeks":
                transaction.setDayOfMonthMonthly(null);
                transaction.setDayOfMonth(null);
                transaction.setMonthOfYear(null);
                break;
            case "years":
                transaction.setDayOfMonthMonthly(null);
                transaction.setDayOfWeek(null);
                break;
            case "days":
                transaction.setDayOfMonthMonthly(null);
                transaction.setDayOfMonth(null);
                transaction.setDayOfWeek(null);
                transaction.setMonthOfYear(null);
                break;
            default:
                // 未知の間隔単位が指定された場合のエラーハンドリング
                break;
        }

        transaction.setInterval(1);
        LocalDate nextTransactionDate = recurringTransactionService.calculateNextTransactionDate(transaction);
        transaction.setNextTransactionDate(nextTransactionDate);

        recurringTransactionService.saveRecurringTransaction(transaction);
        return "redirect:/settings/recurring-transactions";
    }

    @GetMapping("/recurring-transactions/edit/{id}")
    public String editRecurringTransaction(@PathVariable @NonNull Long id, Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId(); // ユーザーIDの取得
        RecurringTransaction transaction = recurringTransactionService.findRecurringTransactionById(id).orElse(null);

        // 対象が存在しない、または対象の所有者がログインユーザーでない場合はアクセス制限
        if (transaction == null || !transaction.getUser().getId().equals(userId)) {
            // アクセス拒否の処理
            throw new AccessDeniedException("このページにアクセスする権限がありません。");
        }
        BigDecimal amount = transaction.getAmount();
        transaction.setAmount(amount.setScale(0, RoundingMode.DOWN));
        model.addAttribute("transaction", transaction);
        model.addAttribute("categories", categoryService.findCategoriesByUserId(userId));
        model.addAttribute("title", "定期入力の編集 - simplesave");
        model.addAttribute("content", "edit-recurring-transaction");
        return "layout";
    }

    @PostMapping("/recurring-transactions/edit/{id}")
    public String updateRecurringTransaction(@PathVariable @NonNull Long id,
            @ModelAttribute RecurringTransaction transaction,
            @NonNull @RequestParam("categoryId") Long categoryId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId(); // ユーザーIDの取得
        if (userId != null) {
            User user = userService.findUserById(userId).orElse(null);
            transaction.setUser(user);
        }

        RecurringTransaction transactionForConfirm = recurringTransactionService.findRecurringTransactionById(id)
                .orElse(null);
        // 対象の所有者がログインユーザーでない場合はアクセス制限
        if (!transactionForConfirm.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("このページにアクセスする権限がありません。");
        }
        Category category = categoryService.findCategoryById(categoryId).orElse(null);
        transaction.setCategory(category);

        transaction.setInterval(1);
        transaction.setNextTransactionDate(null); // 更新の場合、一度次回取引日をリセット

        LocalDate nextTransactionDate = recurringTransactionService.calculateNextTransactionDate(transaction);
        transaction.setNextTransactionDate(nextTransactionDate);

        recurringTransactionService.saveRecurringTransaction(transaction);
        return "redirect:/settings/recurring-transactions";
    }

    @PostMapping("/recurring-transactions/delete/{id}")
    public String deleteRecurringTransaction(@NonNull @PathVariable Long id, RedirectAttributes redirectAttribtes,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId(); // ユーザーIDの取得

        RecurringTransaction transaction = recurringTransactionService.findRecurringTransactionById(id).orElse(null);

        // 対象が存在しない、または対象の所有者がログインユーザーでない場合はアクセス制限
        if (transaction == null || !transaction.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("このページにアクセスする権限がありません。");
        }
        recurringTransactionService.deleteRecurringTransacition(id);
        return "redirect:/settings/recurring-transactions";
    }
}
