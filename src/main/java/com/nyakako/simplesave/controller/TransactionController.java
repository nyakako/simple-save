package com.nyakako.simplesave.controller;

import com.nyakako.simplesave.model.Category;
import com.nyakako.simplesave.model.Transaction;
import com.nyakako.simplesave.service.CategoryService;
import com.nyakako.simplesave.service.TransactionService;

import java.time.LocalDate;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TransactionController {

    private final TransactionService transactionService;
    private final CategoryService categoryService;

    public TransactionController(TransactionService transactionService, CategoryService categoryService) {
        this.transactionService = transactionService;
        this.categoryService = categoryService;
    }

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
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("title", "新規明細登録 - simplesave");
        model.addAttribute("content", "new-transaction");
        return "layout";
    }

    @PostMapping("/transactions/new")
    public String addTransaction(@NonNull @ModelAttribute Transaction transaction,
            @NonNull @RequestParam("categoryId") Long categoryId) {
        // categoryIdを使用してCategoryオブジェクトを取得
        Category category = categoryService.findCategoryById(categoryId).orElse(null);

        // TransactionオブジェクトにCategoryをセット
        transaction.setCategory(category);
        transactionService.saveTransaction(transaction);
        return "redirect:/transactions";
    }

    @GetMapping("/transactions/edit/{id}")
    public String editTransaction(@PathVariable @NonNull Long id, Model model) {
        Transaction transaction = transactionService.findTransactionById(id).orElse(null);
        ;
        model.addAttribute("transaction", transaction);
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("title", "明細編集 - simplesave");
        model.addAttribute("content", "edit-transaction");
        return "layout";
    }

    @PostMapping("/transactions/edit/{id}")
    public String updateTransaction(@PathVariable Long id, @NonNull @ModelAttribute Transaction transaction,
            @NonNull @RequestParam("categoryId") Long categoryId) {
        // categoryIdを使用してCategoryオブジェクトを取得
        Category category = categoryService.findCategoryById(categoryId).orElse(null);

        // TransactionオブジェクトにCategoryをセット
        transaction.setCategory(category);
        transactionService.saveTransaction(transaction);
        return "redirect:/transactions";
    }

    @PostMapping("/transactions/delete/{id}")
    public String deleteTransaction(@NonNull @PathVariable Long id) {
        transactionService.deleteTransacition(id);
        return "redirect:/transactions";
    }
}
