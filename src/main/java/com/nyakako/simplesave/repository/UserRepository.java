package com.nyakako.simplesave.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nyakako.simplesave.model.User;


public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String username);
}
