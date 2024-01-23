package com.nyakako.simplesave.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class RecurringTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private double amount;

    @ManyToOne
    private Category category;

    @Column(name = "interval_value")
    private int interval;
    
    private String intervalUnit;
    private LocalDate startDate;
    private LocalDate nextTransactionDate;
    private LocalDate endDate;
    private String description;
}
