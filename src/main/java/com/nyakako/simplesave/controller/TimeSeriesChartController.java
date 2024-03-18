package com.nyakako.simplesave.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class TimeSeriesChartController {

    @GetMapping("/time-series-chart")
    public String showTimeSeriesChart(Model model, Authentication authentication, HttpSession session,
            @RequestParam(name = "yearMonth", required = false) String yearMonth) {

        // CustomUserDetails userDetails = (CustomUserDetails)
        // authentication.getPrincipal();
        // Long userId = userDetails.getUserId();
        // if (userId != null) {
        // colorPreference = userService.getColorPreference(userId);
        // }

        // LocalDate currentDate = yearMonth == null ? LocalDate.now()
        // : LocalDate.parse(yearMonth + "-01", DateTimeFormatter.ISO_DATE);
        // LocalDate previousMonth = currentDate.minusMonths(1);
        // LocalDate nextMonth = currentDate.plusMonths(1);

        // Map<String, BigDecimal> summary =
        // dashboardService.calculateMonthlySummary(userId, currentDate);
        // List<CategorySum> dataExpenseCategory =
        // dashboardService.getMonthlyExpensesByCategory(userId, currentDate);
        // List<CategorySum> dataIncomeCategory =
        // dashboardService.getMonthlyIncomesByCategory(userId, currentDate);

        // List<Transaction> transactions =
        // dashboardService.getCurrentMonthTransactionsForUser(userId, currentDate);

        // 全ユーザー明細取得（デバック用）
        // List<Transaction> transactions = transactionService.findAllTransactions();
        // model.addAttribute("transactions", transactions);

        model.addAttribute("title", "推移 - simplesave");
        model.addAttribute("content", "time-series-chart");
        // model.addAttribute("currentDate",
        // currentDate.format(DateTimeFormatter.ofPattern("yyyy年MM月")));
        // model.addAttribute("previousMonth",
        // previousMonth.format(DateTimeFormatter.ofPattern("yyyy-MM")));
        // model.addAttribute("nextMonth",
        // nextMonth.format(DateTimeFormatter.ofPattern("yyyy-MM")));
        // model.addAttribute("categoryExpenses", dataExpenseCategory);
        // model.addAttribute("categoryIncomes", dataIncomeCategory);
        return "layout";
    }
}
