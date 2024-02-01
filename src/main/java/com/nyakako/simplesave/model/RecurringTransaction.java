package com.nyakako.simplesave.model;

import java.time.DayOfWeek;
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
    private DayOfWeek dayOfWeek;         // intervalUnitがweeksの場合、週のどの日（例：1 = 月曜日, 2 = 火曜日, ...）
    private Integer dayOfMonthMonthly; // intervalUnitがmonthsの場合、月のどの日（例：25日）
    private Integer dayOfMonth;        // intervalUnitがyearsの場合、月のどの日（例：25日）
    private Integer monthOfYear;       // intervalUnitがyearsの場合、年のどの月（例：2月）
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate nextTransactionDate;
}
