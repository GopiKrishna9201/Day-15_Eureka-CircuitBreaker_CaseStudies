package com.example.userprofileservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.userprofileservice.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

}
