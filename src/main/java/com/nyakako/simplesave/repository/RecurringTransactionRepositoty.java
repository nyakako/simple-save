package com.nyakako.simplesave.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nyakako.simplesave.model.RecurringTransaction;
import java.util.List;
import java.time.LocalDate;


public interface RecurringTransactionRepositoty extends JpaRepository<RecurringTransaction, Long>{
    List<RecurringTransaction> findByNextTransactionDate(LocalDate date);
    List<RecurringTransaction> findByUserId(Long userId);
}
