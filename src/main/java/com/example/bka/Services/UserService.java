package com.example.bka.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.bka.Models.User;
import com.example.bka.Repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    public User userRegistasi(User user)
    {
        User userCek = userRepository.findByEmail(user.getEmail());
        if (userCek == null) {
            return userRepository.save(user);
        }
        throw new IllegalArgumentException("Email already exists");
    }
    
    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;

        }
        throw new IllegalArgumentException("Invalid email or password");
    }
    public User saveUser(User user, Double amount) {
        Double balance = user.getBalance();
        if (balance == null) {
            balance = 0.0;
        }
        user.setBalance(user.getBalance() + amount);
        return userRepository.save(user);
    }
    
    public User updateUser(User user) {
        return userRepository.save(user);
    }
   
}
