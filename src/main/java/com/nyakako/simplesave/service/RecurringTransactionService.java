package com.nyakako.simplesave.service;

import java.time.LocalDate;
import java.util.List;

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
    
    public RecurringTransactionService(RecurringTransactionRepositoty recurringTransactionRepositoty, TransactionRepository transactionRepository) {
        this.recurringTransactionRepositoty = recurringTransactionRepositoty;
        this.transactionRepository = transactionRepository;
    }

    public List<RecurringTransaction> findAllRecurringTransaction() {
        return recurringTransactionRepositoty.findAll();
    }

    @Scheduled(cron = "0 0 1 * * ?") //毎日午前1時に実行
    public void processRecurringTransactions() {

        LocalDate today = LocalDate.now();
        List<RecurringTransaction> recurringTransactions = recurringTransactionRepositoty.findByNextTransactionDate(today);

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

    private LocalDate calculateNextTransactionDate(RecurringTransaction rt) {
        LocalDate nextDate = rt.getNextTransactionDate();
        
        switch (rt.getIntervalUnit()) {
            case "Days":
                nextDate = nextDate.plusDays(rt.getInterval());
                break;
            case "Weeks":
                nextDate = nextDate.plusWeeks(rt.getInterval());
                break;
            case "Months":
                nextDate = nextDate.plusMonths(rt.getInterval());
                break;
            case "Years":
                nextDate = nextDate.plusYears(rt.getInterval());
                break;
            default:
                throw new  IllegalArgumentException("間隔が正しくありません");
        }
        return nextDate;
    }
}
