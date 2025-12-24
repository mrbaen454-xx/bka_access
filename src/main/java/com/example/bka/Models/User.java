package com.example.bka.Models;

import com.example.bka.Constans.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;
    @Email    
    private String email;
    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    private Double balance = 0.0;

    
}
