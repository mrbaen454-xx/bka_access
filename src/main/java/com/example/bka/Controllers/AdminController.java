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
    public String updateProfile(User user,HttpSession session, Model model) { 
        try {
            User updatedUser = userService.updateUser(user);
    
            session.setAttribute("userLogin", updatedUser);
    
            return "redirect:/profile/admin";
            
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            User users = (User) session.getAttribute("userLogin");
            model.addAttribute("user", users);
            return "profileAdmin";
        }
    }
    @GetMapping("/tiketAll")
    public String tiketAll(Model model) {
        try{
            model.addAttribute("tikets",seatBookingService.getAllSeatBooking());
             return "tiketAll";

        }catch(IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
            return "tiketAll";
        }
       
    }

}
