package com.nyakako.simplesave.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.nyakako.simplesave.model.Category;
import com.nyakako.simplesave.model.RecurringTransaction;
import com.nyakako.simplesave.service.CategoryService;
import com.nyakako.simplesave.service.RecurringTransactionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RecurringTransactionController {
    private final RecurringTransactionService recurringTransactionService;
    private final CategoryService categoryService;

    public RecurringTransactionController(RecurringTransactionService recurringTransactionService,
            CategoryService categoryService) {
        this.recurringTransactionService = recurringTransactionService;
        this.categoryService = categoryService;
    }

    @GetMapping("/settings/recurring-transactions")
    public String showRecurringTransactions(Model model) {
        List<RecurringTransaction> transactions = recurringTransactionService.findAllRecurringTransaction();
        model.addAttribute("transactions", transactions);
        model.addAttribute("title", "定期取引一覧 - simplesave");
        model.addAttribute("content", "recurring-transactions");
        return "layout";
    }

    @GetMapping("/recurring-transactions/new")
    public String newRecurringTransaction(Model model) {
        LocalDate today = LocalDate.now();
        LocalDate oneMonthLater = today.plusMonths(1);
        model.addAttribute("today", today);
        model.addAttribute("oneMonthLater", oneMonthLater);
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("title", "新規明細登録 - simplesave");
        model.addAttribute("content", "new-recurring-transaction");
        return "layout";
    }

    @PostMapping("/recurring-transactions/new")
    public String addRecurringTransaction(@ModelAttribute RecurringTransaction transaction, @NonNull @RequestParam("categoryId") Long categoryId) {
        // categoryIdを使用してCategoryオブジェクトを取得
        Category category = categoryService.findCategoryById(categoryId).orElse(null);

        // TransactionオブジェクトにCategoryをセット
        transaction.setCategory(category);
        LocalDate nextTransactionDate = recurringTransactionService.calculateNextTransactionDate(transaction);
        transaction.setNextTransactionDate(nextTransactionDate);
        transaction.setInterval(1);
        recurringTransactionService.saveRecurringTransaction(transaction);
        return "redirect:/settings/recurring-transactions";
    }

}
