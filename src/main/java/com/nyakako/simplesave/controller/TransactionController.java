package com.nyakako.simplesave.controller;

import com.nyakako.simplesave.model.Category;
import com.nyakako.simplesave.model.Transaction;
import com.nyakako.simplesave.model.User;
import com.nyakako.simplesave.security.CustomUserDetails;
import com.nyakako.simplesave.service.CategoryService;
import com.nyakako.simplesave.service.TransactionService;
import com.nyakako.simplesave.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
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
    private final UserService userService;

    public TransactionController(TransactionService transactionService, CategoryService categoryService,
            UserService userService) {
        this.transactionService = transactionService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping("/transactions")
    public String showTransactions(Model model, Authentication authentication) {
        // ログインユーザーの明細取得
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId(); // ユーザーIDの取得
        Iterable<Transaction> transactions = transactionService.findTransactionsByUserId(userId);

        // 全ユーザー明細取得（デバック用）
        // List<Transaction> transactions = transactionService.findAllTransactions();
        model.addAttribute("transactions", transactions);
        model.addAttribute("title", "取引一覧 - simplesave");
        model.addAttribute("content", "transactions");
        return "layout";
    }

    @GetMapping("/transactions/new")
    public String newTransaction(Model model, Authentication authentication, HttpServletRequest request,
            HttpSession session) {
        LocalDate today = LocalDate.now();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId(); // ユーザーIDの取得
        model.addAttribute("today", today);
        model.addAttribute("categories", categoryService.findCategoriesByUserId(userId));
        model.addAttribute("title", "新規明細登録 - simplesave");
        model.addAttribute("content", "new-transaction");

        String returnUrl = (String) session.getAttribute("redirectUrlTransaction");
        if (returnUrl == null) {
            String referrer = request.getHeader("Referer");
            session.setAttribute("redirectUrlTransaction", referrer);
        }

        return "layout";
    }

    @PostMapping("/transactions/new")
    public String addTransaction(@NonNull @ModelAttribute Transaction transaction,
            @NonNull @RequestParam("categoryId") Long categoryId, Authentication authentication, HttpSession session) {

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

        transactionService.saveTransaction(transaction);
        String redirectUrlTransaction = (String) session.getAttribute("redirectUrlTransaction");
        session.removeAttribute("redirectUrlTransaction");

        return "redirect:" + (redirectUrlTransaction != null ? redirectUrlTransaction : "/transactions");
    }

    @GetMapping("/transactions/edit/{id}")
    public String editTransaction(@PathVariable @NonNull Long id, Model model, Authentication authentication,
            HttpServletRequest request,
            HttpSession session) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId(); // ユーザーIDの取得

        Transaction transaction = transactionService.findTransactionById(id).orElse(null);
        // 対象が存在しない、または対象の所有者がログインユーザーでない場合はアクセス制限
        if (transaction == null || !transaction.getUser().getId().equals(userId)) {
            // アクセス拒否の処理
            throw new AccessDeniedException("このページにアクセスする権限がありません。");
        }

        BigDecimal amount = transaction.getAmount();
        transaction.setAmount(amount.setScale(0, RoundingMode.DOWN));
        model.addAttribute("transaction", transaction);
        model.addAttribute("categories", categoryService.findCategoriesByUserId(userId));
        model.addAttribute("title", "明細編集 - simplesave");
        model.addAttribute("content", "edit-transaction");

        String returnUrl = (String) session.getAttribute("redirectUrlTransaction");
        if (returnUrl == null) {
            String referrer = request.getHeader("Referer");
            session.setAttribute("redirectUrlTransaction", referrer);
        }

        return "layout";
    }

    @PostMapping("/transactions/edit/{id}")
    public String updateTransaction(@PathVariable @NonNull Long id, @NonNull @ModelAttribute Transaction transaction,
            @NonNull @RequestParam("categoryId") Long categoryId, Authentication authentication, HttpSession session) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId(); // ユーザーIDの取得
        if (userId != null) {
            User user = userService.findUserById(userId).orElse(null);
            transaction.setUser(user);
        }

        Transaction transactionForConfirm = transactionService.findTransactionById(id).orElse(null);
        // 対象の所有者がログインユーザーでない場合はアクセス制限
        if (!transactionForConfirm.getUser().getId().equals(userId)) {
            // アクセス拒否の処理
            throw new AccessDeniedException("このページにアクセスする権限がありません。");
        }
        Category category = categoryService.findCategoryById(categoryId).orElse(null);
        transaction.setCategory(category);

        transactionService.saveTransaction(transaction);
        String redirectUrlTransaction = (String) session.getAttribute("redirectUrlTransaction");
        session.removeAttribute("redirectUrlTransaction");

        return "redirect:" + (redirectUrlTransaction != null ? redirectUrlTransaction : "/transactions");
    }

    @PostMapping("/transactions/delete/{id}")
    public String deleteTransaction(@NonNull @PathVariable Long id, Authentication authentication,
            HttpSession session) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId(); // ユーザーIDの取得

        Transaction transaction = transactionService.findTransactionById(id).orElse(null);
        // 対象が存在しない、または対象の所有者がログインユーザーでない場合はアクセス制限
        if (transaction == null || !transaction.getUser().getId().equals(userId)) {
            // アクセス拒否の処理
            throw new AccessDeniedException("このページにアクセスする権限がありません。");
        }
        transactionService.deleteTransacition(id);
        String redirectUrlTransaction = (String) session.getAttribute("redirectUrlTransaction");
        session.removeAttribute("redirectUrlTransaction");

        return "redirect:" + (redirectUrlTransaction != null ? redirectUrlTransaction : "/transactions");
    }
}
