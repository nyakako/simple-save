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

    public List<RecurringTransaction> findRecurringTransactionsByUserId(Long userId) {
        return recurringTransactionRepositoty.findByUserId(userId);
    }


    public Optional<RecurringTransaction> findRecurringTransactionById(@NonNull Long id) {
        return recurringTransactionRepositoty.findById(id);
    }

    public void saveRecurringTransaction(@NonNull RecurringTransaction recurringTransaction) {
        recurringTransactionRepositoty.save(recurringTransaction);
    }

    public void deleteRecurringTransacition(@NonNull Long id) {
        recurringTransactionRepositoty.deleteById(id);
    }

    // @Scheduled(cron = "0 0 1 * * ?") // 毎日午前1時に実行
    @Scheduled(fixedRate = 600000) // 60秒毎に実行
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
        boolean isBaseDayTheNextTransactionDate = false; // 今日が次回取引日かどうか

        // 初期登録時or設定更新時は
        // 開始日が間隔の条件を満たす日か確認し満たせば開始日
        // そうでなければ計算し求める
        if (transaction.getNextTransactionDate() == null) {

            // 開始日が今日より前の場合、今日を基準にする
            if (transaction.getStartDate().isBefore(today)) {
                baseDate = today;
            } else {
                // そうでなければ開始日を基準にする
                baseDate = transaction.getStartDate();
            }
            isBaseDayTheNextTransactionDate = isBaseDayMatchingCondition(transaction, baseDate);

            // 基準日が条件を満たす場合は基準日を次回取引日とする
            if (isBaseDayTheNextTransactionDate) {
                return baseDate;
            }
        } else {
            // スケジュール実行後は
            // 既存の次回取引日を基準に計算する
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

    private boolean isBaseDayMatchingCondition(RecurringTransaction transaction, LocalDate baseDate) {
        switch (transaction.getIntervalUnit()) {
            case "days":
                return true; // 毎日が条件なので常にtrue
            case "weeks":
                return baseDate.getDayOfWeek() == transaction.getDayOfWeek(); // 曜日が一致するか
            case "months":
                return baseDate.getDayOfMonth() == transaction.getDayOfMonthMonthly(); // 日にちが一致するか
            case "years":
                return baseDate.getMonthValue() == transaction.getMonthOfYear()
                        && baseDate.getDayOfMonth() == transaction.getDayOfMonth(); // 月と日にちが一致するか
            default:
                return false;
        }
    }

}
