package com.nyakako.simplesave.controller;

import com.nyakako.simplesave.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

}
