package com.nyakako.simplesave.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nyakako.simplesave.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    
}
