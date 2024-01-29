package com.nyakako.simplesave.controller;

import com.nyakako.simplesave.model.Category;
import com.nyakako.simplesave.model.Transaction;
import com.nyakako.simplesave.service.CategoryService;
import com.nyakako.simplesave.service.TransactionService;

import java.time.LocalDate;
import java.util.List;

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

    @GetMapping("/settings/categories-expense")
    public String showExpenseCategory(Model model) {
        List<Category> expenseCategories = categoryService.findCategoriesByType("expense");
        model.addAttribute("expenseCategories", expenseCategories);
        model.addAttribute("title", "支出カテゴリ設定 - simplesave");
        model.addAttribute("content", "categories-expense");
        return "layout";
    }

    @GetMapping("/settings/categories-income")
    public String showIncomeCategory(Model model) {
        List<Category> incomeCategories = categoryService.findCategoriesByType("income");
        model.addAttribute("expenseCategories", incomeCategories);
        model.addAttribute("title", "収入カテゴリ設定 - simplesave");
        model.addAttribute("content", "categories-income");
        return "layout";
    }

    @GetMapping("/categories-expense/new")
    public String newExpenseCategory(Model model) {
        model.addAttribute("title", "支出カテゴリ登録 - simplesave");
        model.addAttribute("content", "new-expense-category");
        return "layout";
    }

    @PostMapping("/categories-expense/new")
    public String addExpenseCategory(@NonNull @ModelAttribute Category category) {
        category.setType("expense");
        categoryService.saveCategory(category);
        return "redirect:/settings/categories-expense";
    }

    @GetMapping("/categories-expense/edit/{id}")
    public String editExpenseCategory(@PathVariable @NonNull Long id, Model model) {
        Category category = categoryService.findCategoryById(id).orElse(null);
        model.addAttribute("category", category);
        model.addAttribute("title", "支出カテゴリ編集 - simplesave");
        model.addAttribute("content", "edit-expense-category");
        return "layout";
    }

    @PostMapping("/categories-expense/edit/{id}")
    public String updateExpenseCategory(@PathVariable Long id, @NonNull @ModelAttribute Category category) {
        category.setType("expense");
        categoryService.saveCategory(category);
        return "redirect:/settings/categories-expense";
    }
}
