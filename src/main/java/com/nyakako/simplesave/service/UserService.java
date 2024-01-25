package com.nyakako.simplesave.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nyakako.simplesave.model.User;
import com.nyakako.simplesave.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
