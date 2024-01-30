package com.nyakako.simplesave.service;

import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.nyakako.simplesave.model.Category;
import com.nyakako.simplesave.repository.CategoryRepository;
import com.nyakako.simplesave.repository.TransactionRepository;

@Service
public class CategoryService {
    
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository, TransactionRepository transactionRepository) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
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

    public void deleteCategory(@NonNull Long id) {
        categoryRepository.deleteById(id);
    }

    public boolean isCategoryUsed(Long categoryId) {
        return transactionRepository.existsByCategoryId(categoryId);
    }
}
