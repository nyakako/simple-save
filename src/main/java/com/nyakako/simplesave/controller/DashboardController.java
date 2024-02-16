package com.nyakako.simplesave.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nyakako.simplesave.model.Transaction;
import com.nyakako.simplesave.repository.TransactionRepository.CategorySum;
import com.nyakako.simplesave.security.CustomUserDetails;
import com.nyakako.simplesave.service.DashboardService;
import com.nyakako.simplesave.service.UserService;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserService userService;

    public DashboardController(DashboardService dashboardService, UserService userService) {
        this.dashboardService = dashboardService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Authentication authentication,
            @RequestParam(name = "yearMonth", required = false) String yearMonth) {
        String colorPreference = "greenPositive";
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();
        if (userId != null) {
            colorPreference = userService.getColorPreference(userId);
        }

        LocalDate currentDate = yearMonth == null ? LocalDate.now()
                : LocalDate.parse(yearMonth + "-01", DateTimeFormatter.ISO_DATE);
        LocalDate previousMonth = currentDate.minusMonths(1);
        LocalDate nextMonth = currentDate.plusMonths(1);

        Map<String, BigDecimal> summary = dashboardService.calculateMonthlySummary(userId, currentDate);
        List<CategorySum> dataExpenseCategory = dashboardService.getMonthlyExpensesByCategory(userId, currentDate);
        List<CategorySum> dataIncomeCategory = dashboardService.getMonthlyIncomesByCategory(userId, currentDate);

        List<Transaction> transactions = dashboardService.getCurrentMonthTransactionsForUser(userId,currentDate);

        // 全ユーザー明細取得（デバック用）
        // List<Transaction> transactions = transactionService.findAllTransactions();
        model.addAttribute("transactions", transactions);

        model.addAttribute("title", "ダッシュボード - simplesave");
        model.addAttribute("content", "dashboard");
        model.addAttribute("colorPreference", colorPreference);
        model.addAttribute("currentDate", currentDate.format(DateTimeFormatter.ofPattern("yyyy年MM月")));
        model.addAttribute("previousMonth", previousMonth.format(DateTimeFormatter.ofPattern("yyyy-MM")));
        model.addAttribute("nextMonth", nextMonth.format(DateTimeFormatter.ofPattern("yyyy-MM")));
        model.addAttribute("summary", summary);
        model.addAttribute("categoryExpenses", dataExpenseCategory);
        model.addAttribute("categoryIncomes", dataIncomeCategory);
        return "layout";
    }

}
