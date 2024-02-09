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

    // ユーザー登録時の自動カテゴリ追加のために必要なコンストラクタ
    public Category(String name, String type, User user) {
        this.name = name;
        this.type = type;
        this.user = user;
    }
}
