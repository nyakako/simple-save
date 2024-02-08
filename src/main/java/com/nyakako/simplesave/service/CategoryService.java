package com.nyakako.simplesave.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.nyakako.simplesave.model.Category;
import com.nyakako.simplesave.model.User;
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

    public List<Category> findCategoriesByUserId(Long userId) {
        return categoryRepository.findByUserId(userId);
    }

    public Optional<Category> findCategoryById(@NonNull Long id) {
        return categoryRepository.findById(id);
    }

    public List<Category> findCategoriesByTypeAndUserId(String type, Long userId) {
        return categoryRepository.findByTypeAndUserId(type, userId);
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

    public void createDefaultCategoriesForUser(User user) {
        List<Category> defaultCategories = Arrays.asList(
                new Category("食費", "expense", user),
                new Category("交通費", "expense", user),
                new Category("デフォルト", "expense", user),
                new Category("給与", "income", user),
                new Category("賞与", "income", user),
                new Category("デフォルト", "income", user)
        );

        for (Category category : defaultCategories) {
            category.setUser(user);
            categoryRepository.save(category);
        }
    }
}
