package com.example.bka.Models;

import com.example.bka.Constans.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;    
    private String email;
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    private Double balance = 0.0;

    
}
