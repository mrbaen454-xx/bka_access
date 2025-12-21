package com.example.bka.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bka.Constans.Role;
import com.example.bka.Models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByRole(Role role);
}
