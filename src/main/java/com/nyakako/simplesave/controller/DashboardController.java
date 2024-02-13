package com.nyakako.simplesave.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.nyakako.simplesave.repository.TransactionRepository.CategorySum;
import com.nyakako.simplesave.security.CustomUserDetails;
import com.nyakako.simplesave.service.DashboardService;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId(); // ユーザーIDの取得

        Map<String, BigDecimal> summary = dashboardService.calculateMonthlySummary(userId);
        List<CategorySum> dataExpenseCategory = dashboardService.getMonthlyExpensesByCategory(userId);

        model.addAttribute("title", "ダッシュボード - simplesave");
        model.addAttribute("content", "dashboard");
        model.addAttribute("summary", summary);
        model.addAttribute("categoryExpenses", dataExpenseCategory);
        return "layout";
    }

}
