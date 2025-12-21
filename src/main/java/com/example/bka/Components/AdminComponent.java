package com.example.bka.Components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.bka.Constans.Role;
import com.example.bka.Models.User;
import com.example.bka.Repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Component
public class AdminComponent {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void adminDate()
    {
        if (userRepository.findAll().isEmpty()) {
            
            User user = new User();
            user.setName("Admin");
            user.setEmail("admin@gmail.com");
            user.setPassword("admin123");
            user.setRole(Role.ADMIN);
            userRepository.save(user);
        }
    }
}