package com.nyakako.simplesave.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.nyakako.simplesave.model.Category;
import com.nyakako.simplesave.model.RecurringTransaction;
import com.nyakako.simplesave.service.CategoryService;
import com.nyakako.simplesave.service.RecurringTransactionService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RecurringTransactionController {

    private final RecurringTransactionService recurringTransactionService;
    private final CategoryService categoryService;

    public RecurringTransactionController(RecurringTransactionService recurringTransactionService,
            CategoryService categoryService) {
        this.recurringTransactionService = recurringTransactionService;
        this.categoryService = categoryService;
    }

    @GetMapping("/settings/recurring-transactions")
    public String showRecurringTransactions(Model model) {
        List<RecurringTransaction> transactions = recurringTransactionService.findAllRecurringTransaction();
        model.addAttribute("transactions", transactions);
        model.addAttribute("title", "定期取引一覧 - simplesave");
        model.addAttribute("content", "recurring-transactions");
        return "layout";
    }

    @GetMapping("/recurring-transactions/new")
    public String newRecurringTransaction(Model model) {
        LocalDate today = LocalDate.now();
        LocalDate oneMonthLater = today.plusMonths(1);
        model.addAttribute("today", today);
        model.addAttribute("oneMonthLater", oneMonthLater);
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("title", "新規明細登録 - simplesave");
        model.addAttribute("content", "new-recurring-transaction");
        return "layout";
    }

    @PostMapping("/recurring-transactions/new")
    public String addRecurringTransaction(@ModelAttribute RecurringTransaction transaction,
            @NonNull @RequestParam("categoryId") Long categoryId) {
        // categoryIdを使用してCategoryオブジェクトを取得
        Category category = categoryService.findCategoryById(categoryId).orElse(null);

        // TransactionオブジェクトにCategoryをセット
        transaction.setCategory(category);

        // intervalUnitに基づいて登録フィールド以外をクリア
        switch (transaction.getIntervalUnit()) {
            case "months":
                transaction.setDayOfMonth(null);
                transaction.setDayOfWeek(null);
                transaction.setMonthOfYear(null);
                break;
            case "weeks":
                transaction.setDayOfMonthMonthly(null);
                transaction.setDayOfMonth(null);
                transaction.setMonthOfYear(null);
                break;
            case "years":
                transaction.setDayOfMonthMonthly(null);
                transaction.setDayOfWeek(null);
                break;
            case "days":
                transaction.setDayOfMonthMonthly(null);
                transaction.setDayOfMonth(null);
                transaction.setDayOfWeek(null);
                transaction.setMonthOfYear(null);
                break;
            default:
                // 未知の間隔単位が指定された場合のエラーハンドリング
                break;
        }

        transaction.setInterval(1);
        LocalDate nextTransactionDate = recurringTransactionService.calculateNextTransactionDate(transaction);
        transaction.setNextTransactionDate(nextTransactionDate);

        recurringTransactionService.saveRecurringTransaction(transaction);
        return "redirect:/settings/recurring-transactions";
    }

    @GetMapping("/recurring-transactions/edit/{id}")
    public String editRecurringTransaction(@PathVariable @NonNull Long id, Model model) {
        RecurringTransaction transaction = recurringTransactionService.findRecurringTransactionById(id).orElse(null);
        model.addAttribute("transaction", transaction);
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("title", "定期入力の編集 - simplesave");
        model.addAttribute("content", "edit-recurring-transaction");
        return "layout";
    }

    @PostMapping("/recurring-transactions/edit/{id}")
    public String updateRecurringTransaction(@PathVariable Long id, @ModelAttribute RecurringTransaction transaction, @NonNull @RequestParam("categoryId") Long categoryId) {
        Category category = categoryService.findCategoryById(categoryId).orElse(null);
        transaction.setCategory(category);

        transaction.setInterval(1);
        LocalDate nextTransactionDate = recurringTransactionService.calculateNextTransactionDate(transaction);
        transaction.setNextTransactionDate(nextTransactionDate);

        recurringTransactionService.saveRecurringTransaction(transaction);
        return "redirect:/settings/recurring-transactions";
    }

    @PostMapping("/recurring-transactions/delete/{id}")
    public String deleteRecurringTransaction(@NonNull @PathVariable Long id, RedirectAttributes redirectAttribtes) {
        // if (!categoryService.isCategoryUsed(id)) {
        //     categoryService.deleteCategory(id);
        //     redirectAttribtes.addFlashAttribute("successMessage","カテゴリが正常に削除されました" );
        // } else {
        //     redirectAttribtes.addFlashAttribute("errorMessage", "このカテゴリは明細で使用されているため、削除できません。");
        // }
        return "redirect:/settings/recurring-transactions";
    }
}
