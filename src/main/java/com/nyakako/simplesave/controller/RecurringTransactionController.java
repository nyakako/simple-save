package com.nyakako.simplesave.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.nyakako.simplesave.model.RecurringTransaction;
import com.nyakako.simplesave.service.CategoryService;
import com.nyakako.simplesave.service.RecurringTransactionService;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RecurringTransactionController {
    private final RecurringTransactionService recurringTransactionService;
    private final CategoryService categoryService;

    public RecurringTransactionController(RecurringTransactionService recurringTransactionService, CategoryService categoryService) {
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
        model.addAttribute("today", today);
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("title", "新規明細登録 - simplesave");
        model.addAttribute("content", "new-recurring-transaction");
        return "layout";
    }

}
