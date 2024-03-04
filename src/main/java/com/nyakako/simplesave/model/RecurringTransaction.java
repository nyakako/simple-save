package com.nyakako.simplesave.model;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.nyakako.simplesave.util.DayOfWeekConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
    private BigDecimal amount;

    @Column(name = "interval_value")
    private int interval = 1; //intervalUnit=カスタムを未実装の為、常に1

    private String intervalUnit;

    @Convert(converter = DayOfWeekConverter.class)
    private DayOfWeek dayOfWeek; // intervalUnitがweeksの場合、週のどの日（例：MONDAY,TUESDAY...）
    private Integer dayOfMonthMonthly; // intervalUnitがmonthsの場合、月のどの日（例：25日）
    private Integer dayOfMonth; // intervalUnitがyearsの場合、月のどの日（例：25日）
    private Integer monthOfYear; // intervalUnitがyearsの場合、年のどの月（例：2月）

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private LocalDate nextTransactionDate;
}
