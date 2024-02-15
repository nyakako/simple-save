package com.nyakako.simplesave.service;

import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nyakako.simplesave.model.User;
import com.nyakako.simplesave.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CategoryService categoryService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, CategoryService categoryService,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.categoryService = categoryService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User registerUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        // デフォルトカテゴリを作成
        categoryService.createDefaultCategoriesForUser(savedUser);
        return savedUser;
    }

    public Optional<User> findUserById(@NonNull Long id) {
        return userRepository.findById(id);
    }

    public boolean isUsernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean isEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public String getColorPreference(@NonNull Long userId) {
        return userRepository.findById(userId).map(User::getColorPreference).orElse("greenPositive"); // デフォルトを"greenPositive"とする
    }

    @Transactional
    public void updateColorPreference(@NonNull Long userId, String colorPreference) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setColorPreference(colorPreference);
            userRepository.save(user);
        });
    }
}
