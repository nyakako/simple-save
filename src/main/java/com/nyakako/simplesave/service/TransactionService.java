package com.nyakako.simplesave.service;

import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.nyakako.simplesave.model.Transaction;
import com.nyakako.simplesave.repository.TransactionRepository;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Iterable<Transaction> findAllTransactions() {
        return transactionRepository.findAll();
    }

    public Iterable<Transaction> findTransactionsByUserId(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    public Optional<Transaction> findTransactionById(@NonNull Long id) {
        return transactionRepository.findById(id);
    }

    public Transaction saveTransaction(@NonNull Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public void deleteTransacition(@NonNull Long id) {
        transactionRepository.deleteById(id);
    }

}
