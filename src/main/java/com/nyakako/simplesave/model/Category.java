package com.nyakako.simplesave.model;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Category {
    @Id
    private Long id;
    private String name;
}
