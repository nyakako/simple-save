package com.nyakako.simplesave.service;

import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.nyakako.simplesave.model.Category;
import com.nyakako.simplesave.repository.CategoryRepository;

@Service
public class CategoryService {
    
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findCategoryById(@NonNull Long id){
        return categoryRepository.findById(id);
    }

    public List<Category> findCategoriesByType(String type) {
        return categoryRepository.findByType(type);
    }

    public void saveCategory(@NonNull Category category) {
        categoryRepository.save(category);
    }
}
