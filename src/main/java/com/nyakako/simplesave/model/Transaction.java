package com.nyakako.simplesave.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class Transaction {
    @Id
    private Long id;

    @Column(value = "CATEGORYID")
    private Long categoryId;
    
    private String description;
    private Double amount;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
