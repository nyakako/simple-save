package com.nyakako.simplesave.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nyakako.simplesave.model.Transaction;
import com.nyakako.simplesave.repository.TransactionRepository;
import com.nyakako.simplesave.repository.TransactionRepository.CategorySum;


@Service
public class DashboardService {

    private final TransactionRepository transactionRepository;

    public DashboardService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Map<String, BigDecimal> calculateMonthlySummary(Long userId) {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());

        BigDecimal incomeTotal = transactionRepository.sumAmountByTypeAndDateRange(userId, "income", startOfMonth,
                endOfMonth);
        BigDecimal expenseTotal = transactionRepository.sumAmountByTypeAndDateRange(userId, "expense", startOfMonth,
                endOfMonth);

        expenseTotal = expenseTotal == null ? BigDecimal.ZERO : expenseTotal;
        incomeTotal = incomeTotal == null ? BigDecimal.ZERO : incomeTotal;

        BigDecimal netIncome = incomeTotal.subtract(expenseTotal);

        Map<String, BigDecimal> summary = new HashMap<>();
        summary.put("incomeTotal", incomeTotal);
        summary.put("expenseTotal", expenseTotal);
        summary.putIfAbsent("netIncome", netIncome);

        return summary;

    }

    @Transactional(readOnly = true)
    public List<CategorySum> getMonthlyExpensesByCategory(Long userId) {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());
        return transactionRepository.findTotalAmountByCategoryForCurrentUser(userId, "expense", startOfMonth, endOfMonth);
    }

    @Transactional(readOnly = true)
    public List<CategorySum> getMonthlyIncomesByCategory(Long userId) {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());
        return transactionRepository.findTotalAmountByCategoryForCurrentUser(userId, "income", startOfMonth, endOfMonth);
    }

    public List<Transaction> getCurrentMonthTransactionsForUser(Long userId) {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());
        return transactionRepository.findTransactionsForCurrentUserInCurrentMonth(userId, startOfMonth, endOfMonth);
    }

}
