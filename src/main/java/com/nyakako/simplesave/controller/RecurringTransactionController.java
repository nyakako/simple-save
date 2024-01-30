package com.nyakako.simplesave.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.nyakako.simplesave.model.RecurringTransaction;
import com.nyakako.simplesave.service.RecurringTransactionService;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class RecurringTransactionController {
    private final RecurringTransactionService recurringTransactionService;

    public RecurringTransactionController(RecurringTransactionService recurringTransactionService) {
        this.recurringTransactionService = recurringTransactionService;
    }

    @GetMapping("/settings/recurring-transactions")
    public String showRecurringTransactions(Model model) {
        List<RecurringTransaction> transactions = recurringTransactionService.findAllRecurringTransaction();
        model.addAttribute("transactions", transactions);
        model.addAttribute("title", "定期取引一覧 - simplesave");
        model.addAttribute("content", "recurring-transactions");
        return "layout";
    }
    
}
