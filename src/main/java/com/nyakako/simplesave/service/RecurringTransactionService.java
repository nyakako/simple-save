package com.nyakako.simplesave.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.nyakako.simplesave.model.RecurringTransaction;
import com.nyakako.simplesave.model.Transaction;
import com.nyakako.simplesave.repository.RecurringTransactionRepositoty;
import com.nyakako.simplesave.repository.TransactionRepository;




@Service
public class RecurringTransactionService {

    private final RecurringTransactionRepositoty recurringTransactionRepositoty;
    private final TransactionRepository transactionRepository;

    public RecurringTransactionService(RecurringTransactionRepositoty recurringTransactionRepositoty,
            TransactionRepository transactionRepository) {
        this.recurringTransactionRepositoty = recurringTransactionRepositoty;
        this.transactionRepository = transactionRepository;
    }

    public List<RecurringTransaction> findAllRecurringTransaction() {
        return recurringTransactionRepositoty.findAll();
    }

    public void saveRecurringTransaction(@NonNull RecurringTransaction recurringTransaction) {
        recurringTransactionRepositoty.save(recurringTransaction);
    }

    @Scheduled(cron = "0 0 1 * * ?") // 毎日午前1時に実行
    public void processRecurringTransactions() {

        LocalDate today = LocalDate.now();
        List<RecurringTransaction> recurringTransactions = recurringTransactionRepositoty
                .findByNextTransactionDate(today);

        for (RecurringTransaction rt : recurringTransactions) {
            Transaction newTransaction = new Transaction();
            newTransaction.setUser(rt.getUser());
            newTransaction.setDate(today);
            newTransaction.setAmount(rt.getAmount());
            newTransaction.setCategory(rt.getCategory());
            newTransaction.setDescription(rt.getDescription());
            transactionRepository.save(newTransaction);

            rt.setNextTransactionDate(calculateNextTransactionDate(rt));
            recurringTransactionRepositoty.save(rt);
        }
    }

    public LocalDate calculateNextTransactionDate(RecurringTransaction transaction) {
        LocalDate baseDate; // 計算の基準日
        if (transaction.getNextTransactionDate() == null) {
            // 初期登録時は開始日を基準にする
            baseDate = transaction.getStartDate();
        } else {
            // 更新時は既存の次回取引日を基準にする
            baseDate = transaction.getNextTransactionDate();
        }

        LocalDate endDate = transaction.getEndDate();
        String intervalUnit = transaction.getIntervalUnit();
        int intervalValue = transaction.getInterval();

        // 次回取引日の計算
        LocalDate nextTransactionDate = calculateDate(baseDate, intervalUnit, intervalValue);

        // 終了日を超える場合はnullをセット
        if (nextTransactionDate != null && endDate != null && nextTransactionDate.isAfter(endDate)) {
            return null;
        }

        return nextTransactionDate;
    }

    private LocalDate calculateDate(LocalDate date, String intervalUnit, int intervalValue) {
        switch (intervalUnit) {
            case "days":
                return date.plusDays(intervalValue);
            case "weeks":
                return date.plusWeeks(intervalValue);
            case "months":
                return date.plusMonths(intervalValue);
            case "years":
                return date.plusYears(intervalValue);
            default:
                return date;
        }
    }

}
