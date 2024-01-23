package com.nyakako.simplesave.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nyakako.simplesave.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
    
}
