package com.nyakako.simplesave.repository;

import org.springframework.data.repository.CrudRepository;

import com.nyakako.simplesave.model.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    
}
