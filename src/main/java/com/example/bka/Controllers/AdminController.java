package com.example.bka.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.bka.Constans.Role;
import com.example.bka.Models.User;
import com.example.bka.Services.SeatBookingService;
import com.example.bka.Services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
   private SeatBookingService seatBookingService;


    @GetMapping("/admin")
    public String admin(HttpSession session) {
        User user = (User) session.getAttribute("userLogin");
        if (user == null || user.getRole().equals(Role.USER)) {
            return "redirect:/";
        }

        return "admin";
    }

    @GetMapping("/profile/admin")
    public String adminProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("userLogin");
        model.addAttribute("user", user);
        return "profileAdmin";
    }

    @PostMapping("/profile/admin/update")
    public String updateProfile(User user, HttpSession session) {

        User updatedUser = userService.updateUser(user);

        session.setAttribute("userLogin", updatedUser);

        return "redirect:/profile/admin";
    }
    @GetMapping("/tiketAll")
    public String tiketAll(Model model) {
       
       model.addAttribute("tikets",seatBookingService.getAllSeatBooking());
        return "tiketAll";
    }

}
