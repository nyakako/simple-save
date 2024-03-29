package com.nyakako.simplesave.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nyakako.simplesave.model.Category;
import java.util.List;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByTypeAndUserId(String type, Long userId);
    List<Category> findByUserId(Long userId);
}
