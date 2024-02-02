package com.nyakako.simplesave.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(RecurringTransactionService.class);

    public RecurringTransactionService(RecurringTransactionRepositoty recurringTransactionRepositoty,
            TransactionRepository transactionRepository) {
        this.recurringTransactionRepositoty = recurringTransactionRepositoty;
        this.transactionRepository = transactionRepository;
    }

    public List<RecurringTransaction> findAllRecurringTransaction() {
        return recurringTransactionRepositoty.findAll();
    }

    public Optional<RecurringTransaction> findRecurringTransactionById(@NonNull Long id) {
        return recurringTransactionRepositoty.findById(id);
    }

    public void saveRecurringTransaction(@NonNull RecurringTransaction recurringTransaction) {
        recurringTransactionRepositoty.save(recurringTransaction);
    }

    // @Scheduled(cron = "0 0 1 * * ?") // 毎日午前1時に実行
    @Scheduled(fixedRate = 60000) // 60秒毎に実行
    public void processRecurringTransactions() {
        logger.info("Starting processRecurringTransactions...");

        try {
            LocalDate today = LocalDate.now();
            List<RecurringTransaction> recurringTransactions = recurringTransactionRepositoty
                    .findByNextTransactionDate(today);

            for (RecurringTransaction rt : recurringTransactions) {
                Transaction newTransaction = new Transaction();
                // newTransaction.setUser(rt.getUser());
                newTransaction.setDate(today);
                newTransaction.setAmount(rt.getAmount());
                newTransaction.setCategory(rt.getCategory());
                newTransaction.setDescription(rt.getDescription());
                newTransaction.setScheduled(true);
                transactionRepository.save(newTransaction);

                logger.info("Created new transaction with ID: {} Category: {} Amount: {} Date: {}",
                        newTransaction.getId(),
                        newTransaction.getCategory().getName(),
                        newTransaction.getAmount(),
                        newTransaction.getDate());

                rt.setNextTransactionDate(calculateNextTransactionDate(rt));
                recurringTransactionRepositoty.save(rt);
            }
            logger.info("Sucessfully processed recurring transactions.");
        } catch (Exception e) {
            logger.error("Error processing recurring transactions", e);
        }
    }

    public LocalDate calculateNextTransactionDate(RecurringTransaction transaction) {
        LocalDate today = LocalDate.now(); // 今日の日付を取得
        LocalDate baseDate; // 計算の基準日

        if (transaction.getNextTransactionDate() == null) {
            // 初期登録時
            if (transaction.getStartDate().isBefore(today)) {
                // 開始日が今日より前の場合、今日を基準にする
                baseDate = today;
            } else {
                // そうでなければ開始日を基準にする
                baseDate = transaction.getStartDate();
            }
        } else {
            // 更新時は既存の次回取引日を基準にする
            baseDate = transaction.getNextTransactionDate();
        }

        LocalDate endDate = transaction.getEndDate();
        String intervalUnit = transaction.getIntervalUnit();
        int intervalValue = transaction.getInterval();
        LocalDate nextTransactionDate;

        // 次回取引日の計算
        switch (intervalUnit) {
            case "days":
                nextTransactionDate = baseDate.plusDays(intervalValue);
                break;
            case "weeks":
                nextTransactionDate = calculateNextWeekday(baseDate, transaction.getDayOfWeek());
                break;
            case "months":
                nextTransactionDate = calculateNextMonthDay(baseDate, transaction.getDayOfMonthMonthly());
                break;
            case "years":
                nextTransactionDate = calculateNextYearDay(baseDate, transaction.getMonthOfYear(),
                        transaction.getDayOfMonth());
                break;
            default:
                nextTransactionDate = baseDate;
                break;
        }

        // 終了日を超える場合はnullをセット
        if (nextTransactionDate != null && endDate != null && nextTransactionDate.isAfter(endDate)) {
            return null;
        }

        return nextTransactionDate;
    }

    private LocalDate calculateNextWeekday(LocalDate baseDate, DayOfWeek dayOfWeek) {
        int intDayOfWeek = dayOfWeek.getValue();
        int currentDayOfWeek = baseDate.getDayOfWeek().getValue();
        int daysUntilNextDayOfWeek = (intDayOfWeek - currentDayOfWeek + 7) % 7;

        if (daysUntilNextDayOfWeek == 0) {
            daysUntilNextDayOfWeek = 7;
        }

        return baseDate.plusDays(daysUntilNextDayOfWeek);
    }

    private LocalDate calculateNextMonthDay(LocalDate baseDate, int dayOfMonthMonthly) {
        LocalDate nextDate = baseDate.withDayOfMonth(dayOfMonthMonthly);
        if (nextDate.isBefore(baseDate) || nextDate.isEqual(baseDate)) {
            nextDate = baseDate.plusMonths(1).withDayOfMonth(dayOfMonthMonthly);
        }

        return nextDate;
    }

    private LocalDate calculateNextYearDay(LocalDate baseDate, int monthOfYear, int dayOfMonth) {
        LocalDate nextYearDate = LocalDate.of(baseDate.getYear(), monthOfYear, dayOfMonth);
        if (nextYearDate.isBefore(baseDate) || nextYearDate.isEqual(baseDate)) {
            nextYearDate = LocalDate.of(baseDate.getYear() + 1, monthOfYear, dayOfMonth);
        }

        return nextYearDate;
    }

}
