package com.nyakako.simplesave.repository;

import org.springframework.data.repository.CrudRepository;

import com.nyakako.simplesave.model.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    
}
