package com.nyakako.simplesave.controller;

import com.nyakako.simplesave.model.Category;
import com.nyakako.simplesave.service.CategoryService;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/settings/categories-expense")
    public String showExpenseCategory(Model model) {
        List<Category> expenseCategories = categoryService.findCategoriesByType("expense");
        model.addAttribute("expenseCategories", expenseCategories);
        model.addAttribute("title", "支出カテゴリ設定 - simplesave");
        model.addAttribute("content", "categories-expense");
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

    @PostMapping("/categories-expense/delete/{id}")
    public String deleteExpenseCategory(@NonNull @PathVariable Long id, RedirectAttributes redirectAttribtes) {
        if (!categoryService.isCategoryUsed(id)) {
            categoryService.deleteCategory(id);
            redirectAttribtes.addFlashAttribute("successMessage","カテゴリが正常に削除されました" );
        } else {
            redirectAttribtes.addFlashAttribute("errorMessage", "このカテゴリは明細で使用されているため、削除できません。");
        }
        return "redirect:/settings/categories-expense";
    }

    @GetMapping("/settings/categories-income")
    public String showIncomeCategory(Model model) {
        List<Category> incomeCategories = categoryService.findCategoriesByType("income");
        model.addAttribute("incomeCategories", incomeCategories);
        model.addAttribute("title", "収入カテゴリ設定 - simplesave");
        model.addAttribute("content", "categories-income");
        return "layout";
    }

    @GetMapping("/categories-income/new")
    public String newIncomeCategory(Model model) {
        model.addAttribute("title", "収入カテゴリ登録 - simplesave");
        model.addAttribute("content", "new-income-category");
        return "layout";
    }

    @PostMapping("/categories-income/new")
    public String addIncomeCategory(@NonNull @ModelAttribute Category category) {
        category.setType("income");
        categoryService.saveCategory(category);
        return "redirect:/settings/categories-income";
    }

    @GetMapping("/categories-income/edit/{id}")
    public String editIncomeCategory(@PathVariable @NonNull Long id, Model model) {
        Category category = categoryService.findCategoryById(id).orElse(null);
        model.addAttribute("category", category);
        model.addAttribute("title", "収入カテゴリ編集 - simplesave");
        model.addAttribute("content", "edit-income-category");
        return "layout";
    }

    @PostMapping("/categories-income/edit/{id}")
    public String updateIncomeCategory(@PathVariable Long id, @NonNull @ModelAttribute Category category) {
        category.setType("income");
        categoryService.saveCategory(category);
        return "redirect:/settings/categories-income";
    }

    @PostMapping("/categories-income/delete/{id}")
    public String deleteIncomeCategory(@NonNull @PathVariable Long id, RedirectAttributes redirectAttribtes) {
        if (!categoryService.isCategoryUsed(id)) {
            categoryService.deleteCategory(id);
            redirectAttribtes.addFlashAttribute("successMessage","カテゴリが正常に削除されました" );
        } else {
            redirectAttribtes.addFlashAttribute("errorMessage", "このカテゴリは明細で使用されているため、削除できません。");
        }
        return "redirect:/settings/categories-income";
    }
}
