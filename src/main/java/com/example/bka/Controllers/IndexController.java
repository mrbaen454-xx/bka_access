package com.example.bka.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.bka.Constans.Role;
import com.example.bka.Models.User;
import com.example.bka.Services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String loginUser( @ModelAttribute("user") User user,Model model,HttpSession session) {
        try {
            User userLogin = userService.loginUser(user.getEmail(), user.getPassword());
            session.setAttribute("userLogin", userLogin);
            if (userLogin.getRole().equals(Role.ADMIN)) {
                return "redirect:/admin";
            } else {
                return "redirect:/user";
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }        
    }

    @GetMapping("/registrasi")
    public String registasi(Model model) {
        model.addAttribute("user", new User());
        return "registrasi";
    }

    @PostMapping("/registrasi")
    public String registasiUser(@ModelAttribute("user") User user,Model model) {
        try {
            userService.userRegistasi(user);
            return "redirect:/login";
            
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/registrasi";
        }
    }
    
    
}
