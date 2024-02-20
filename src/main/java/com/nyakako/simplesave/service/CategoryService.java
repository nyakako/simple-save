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
                new Category("日用品", "expense", user),
                new Category("趣味、娯楽", "expense", user),
                new Category("交際費", "expense", user),
                new Category("交通費", "expense", user),
                new Category("衣服、美容", "expense", user),
                new Category("健康、医療", "expense", user),
                new Category("教養、教育", "expense", user),
                new Category("水道、光熱費", "expense", user),
                new Category("通信費", "expense", user),
                new Category("税金", "expense", user),
                new Category("保険", "expense", user),
                new Category("大型出費", "expense", user),
                new Category("その他", "expense", user),
                new Category("給与", "income", user),
                new Category("仕送り、お小遣い", "income", user),
                new Category("事業、副業", "income", user),
                new Category("年金", "income", user),
                new Category("配当所得", "income", user),
                new Category("不動産所得", "income", user),
                new Category("臨時収入", "income", user),
                new Category("その他", "income", user)
        );

        for (Category category : defaultCategories) {
            category.setUser(user);
            categoryRepository.save(category);
        }
    }
}
