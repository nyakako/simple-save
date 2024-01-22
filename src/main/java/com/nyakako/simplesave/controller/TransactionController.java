package com.nyakako.simplesave.controller;

import com.nyakako.simplesave.model.Transaction;
import com.nyakako.simplesave.service.TransactionService;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/transactions")
    public String showTransactions(Model model) {
        model.addAttribute("transactions", transactionService.getAllTransactions());
        model.addAttribute("title", "取引一覧 - simplesave");
        model.addAttribute("content", "transactions");
        return "layout";
    }

    @GetMapping("/transactions/new")
    public String newTransaction(Model model) {
        LocalDate today = LocalDate.now();
        model.addAttribute("today", today);
        model.addAttribute("title", "新規明細登録 - simplesave");
        model.addAttribute("content", "new-transaction");
        return "layout";
    }

    @PostMapping("/transactions")
    public String addTransaction(@NonNull @ModelAttribute Transaction transaction) {
        System.out.println("Received date: " + transaction.getDate());
        transactionService.saveTransaction(transaction);
        return "redirect:/transactions";
    }

}
