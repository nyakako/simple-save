package com.nyakako.simplesave.controller;

import com.nyakako.simplesave.model.Category;
import com.nyakako.simplesave.model.User;
import com.nyakako.simplesave.security.CustomUserDetails;
import com.nyakako.simplesave.service.CategoryService;
import com.nyakako.simplesave.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
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
    private final UserService userService;

    public CategoryController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping("/categories-expense/new")
    public String newExpenseCategory(Model model,
            HttpServletRequest request,
            HttpSession session) {
        model.addAttribute("title", "支出カテゴリ登録 - simplesave");
        model.addAttribute("content", "new-expense-category");
        String referrer = request.getHeader("Referer");
        session.setAttribute("redirectUrlCategory", referrer);
        return "layout";
    }

    @PostMapping("/categories-expense/new")
    public String addExpenseCategory(@NonNull @ModelAttribute Category category, Authentication authentication,
            HttpSession session, RedirectAttributes redirectAttribtes) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId(); // ユーザーIDの取得
        if (userId != null) {
            User user = userService.findUserById(userId).orElse(null);
            category.setUser(user);
        }

        category.setType("expense");
        categoryService.saveCategory(category);
        redirectAttribtes.addFlashAttribute("successMessage", "【" + category.getName() + "】が正常に登録されました");
        String redirectUrlCategory = (String) session.getAttribute("redirectUrlCategory");
        session.removeAttribute("redirectUrlCategory");

        return "redirect:" + (redirectUrlCategory != null ? redirectUrlCategory : "/settings/categories-expense");
    }

    @GetMapping("/categories-expense/edit/{id}")
    public String editExpenseCategory(@PathVariable @NonNull Long id, Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId(); // ログインユーザーのIDを取得

        Category category = categoryService.findCategoryById(id).orElse(null);
        // カテゴリが存在しない、またはカテゴリの所有者がログインユーザーでない場合はアクセス制限
        if (category == null || !category.getUser().getId().equals(userId)) {
            // アクセス拒否の処理
            throw new AccessDeniedException("このページにアクセスする権限がありません。");
        }
        model.addAttribute("category", category);
        model.addAttribute("title", "支出カテゴリ編集 - simplesave");
        model.addAttribute("content", "edit-expense-category");
        return "layout";
    }

    @PostMapping("/categories-expense/edit/{id}")
    public String updateExpenseCategory(@PathVariable @NonNull Long id, @NonNull @ModelAttribute Category category,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId(); // ログインユーザーのIDを取得
        if (userId != null) {
            User user = userService.findUserById(userId).orElse(null);
            category.setUser(user);
        }
        Category categoryForConfirm = categoryService.findCategoryById(id).orElse(null);
        // 対象の所有者がログインユーザーでない場合はアクセス制限
        if (!categoryForConfirm.getUser().getId().equals(userId)) {
            // アクセス拒否の処理
            throw new AccessDeniedException("このページにアクセスする権限がありません。");
        }

        category.setType("expense");
        categoryService.saveCategory(category);
        return "redirect:/settings/categories-expense";
    }

    @PostMapping("/categories-expense/delete/{id}")
    public String deleteExpenseCategory(@NonNull @PathVariable Long id, RedirectAttributes redirectAttribtes,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId(); // ログインユーザーのIDを取得
        Category category = categoryService.findCategoryById(id).orElse(null);
        // カテゴリの所有者がログインユーザーでない場合はアクセス制限
        if (!category.getUser().getId().equals(userId)) {
            // アクセス拒否の処理
            throw new AccessDeniedException("このページにアクセスする権限がありません。");
        }
        if (!categoryService.isCategoryUsed(id)) {
            categoryService.deleteCategory(id);
            redirectAttribtes.addFlashAttribute("successMessage",  "【" + category.getName() + "】が正常に削除されました");
        } else {
            redirectAttribtes.addFlashAttribute("errorMessage",  "【" + category.getName() + "】は明細で使用されているため、削除できません。");
        }
        return "redirect:/settings/categories-expense";
    }

    @GetMapping("/categories-income/new")
    public String newIncomeCategory(Model model, HttpServletRequest request,
            HttpSession session) {
        model.addAttribute("title", "収入カテゴリ登録 - simplesave");
        model.addAttribute("content", "new-income-category");
        String referrer = request.getHeader("Referer");
        session.setAttribute("redirectUrlCategory", referrer);
        return "layout";
    }

    @PostMapping("/categories-income/new")
    public String addIncomeCategory(@NonNull @ModelAttribute Category category, Authentication authentication,
            HttpSession session, RedirectAttributes redirectAttribtes) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId(); // ユーザーIDの取得
        if (userId != null) {
            User user = userService.findUserById(userId).orElse(null);
            category.setUser(user);
        }
        category.setType("income");
        categoryService.saveCategory(category);
        redirectAttribtes.addFlashAttribute("successMessage",  "【" + category.getName() + "】が正常に登録されました");
        String redirectUrlCategory = (String) session.getAttribute("redirectUrlCategory");
        session.removeAttribute("redirectUrlCategory");

        return "redirect:" + (redirectUrlCategory != null ? redirectUrlCategory : "/settings/categories-income");
    }

    @GetMapping("/categories-income/edit/{id}")
    public String editIncomeCategory(@PathVariable @NonNull Long id, Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId(); // ログインユーザーのIDを取得

        Category category = categoryService.findCategoryById(id).orElse(null);

        // またはカテゴリの所有者がログインユーザーでない場合はアクセス制限
        if (category == null || !category.getUser().getId().equals(userId)) {
            // アクセス拒否の処理
            throw new AccessDeniedException("このページにアクセスする権限がありません。");
        }
        model.addAttribute("category", category);
        model.addAttribute("title", "収入カテゴリ編集 - simplesave");
        model.addAttribute("content", "edit-income-category");
        return "layout";
    }

    @PostMapping("/categories-income/edit/{id}")
    public String updateIncomeCategory(@PathVariable @NonNull Long id, @NonNull @ModelAttribute Category category,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId(); // ログインユーザーのIDを取得
        if (userId != null) {
            User user = userService.findUserById(userId).orElse(null);
            category.setUser(user);
        }
        Category categoryForConfirm = categoryService.findCategoryById(id).orElse(null);
        // 対象の所有者がログインユーザーでない場合はアクセス制限
        if (!categoryForConfirm.getUser().getId().equals(userId)) {
            // アクセス拒否の処理
            throw new AccessDeniedException("このページにアクセスする権限がありません。");
        }
        category.setType("income");
        categoryService.saveCategory(category);
        return "redirect:/settings/categories-income";
    }

    @PostMapping("/categories-income/delete/{id}")
    public String deleteIncomeCategory(@NonNull @PathVariable Long id, RedirectAttributes redirectAttribtes,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId(); // ログインユーザーのIDを取得

        Category category = categoryService.findCategoryById(id).orElse(null);
        // またはカテゴリの所有者がログインユーザーでない場合はアクセス制限
        if (!category.getUser().getId().equals(userId)) {
            // アクセス拒否の処理
            throw new AccessDeniedException("このページにアクセスする権限がありません。");
        }
        if (!categoryService.isCategoryUsed(id)) {
            categoryService.deleteCategory(id);
            redirectAttribtes.addFlashAttribute("successMessage",  "【" + category.getName() + "】が正常に削除されました");
        } else {
            redirectAttribtes.addFlashAttribute("errorMessage",  "【" + category.getName() + "】は明細で使用されているため、削除できません。");
        }
        return "redirect:/settings/categories-income";
    }
}
