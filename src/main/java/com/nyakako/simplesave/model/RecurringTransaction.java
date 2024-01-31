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

    @ManyToOne
    private Category category;
    
    private String description;
    private double amount;
    
    @Column(name = "interval_value")
    private int interval;
    
    private String intervalUnit;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate nextTransactionDate;
}
