package com.nyakako.simplesave.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Transaction {
    @Id
    private Long id;
    private String category;
    private String description;
    private Double amount;
    private LocalDate date;
}
