package com.nyakako.simplesave.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type; // "income" or "expense"

    @ManyToOne
    private User user;

    public Category() {
    }

    // 明示的に必要なコンストラクタを追加
    public Category(String name, String type, User user) {
        this.name = name;
        this.type = type;
        this.user = user;
    }
}
