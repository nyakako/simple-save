package com.nyakako.simplesave.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.nyakako.simplesave.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    boolean existsByCategoryId(Long categoryId);
}
