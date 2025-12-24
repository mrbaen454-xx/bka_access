package com.example.bka.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.bka.Constans.Role;
import com.example.bka.Models.User;
import com.example.bka.Services.ScheduleService;
import com.example.bka.Services.StationService;
import com.example.bka.Services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserCotroller {

    @Autowired
    private UserService userService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private StationService stationService;

    @GetMapping("/user")
    public String user(Model model, HttpSession session) {
         User user = (User) session.getAttribute("userLogin");
        if (user == null || user.getRole().equals(Role.ADMIN)) {
            return "redirect:/";
        }
        model.addAttribute("stationByCity", stationService.getStationsGroupedByCity());
        return "user";
    }

    @GetMapping("/serchSchedule")
    public String serchSchedule(@RequestParam("departureStation") String departureStation,
            @RequestParam("arrivalStation") String arrivalStation, Model model) {
                try {
                    
                    model.addAttribute("schedules", scheduleService.serch(departureStation, arrivalStation));
            
                    return "serchSchedule";
                } catch (IllegalArgumentException e) {
                   model.addAttribute("error", e.getMessage());
                   return "serchSchedule";
                }
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {

        User user = (User) session.getAttribute("userLogin");
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/topUp")
    public String processTopUp(@RequestParam("amount") Double amount, HttpSession session) {
        User user = (User) session.getAttribute("userLogin");

        userService.saveUser(user, amount);
        session.setAttribute("userLogin", user);

        return "redirect:/profile";
    }
    
    @PostMapping("/profile/update")
    public String updateProfile(User user, HttpSession session) {
        User updatedUser = userService.updateUser(user);

        session.setAttribute("userLogin", updatedUser);

        return "redirect:/profile";
    }

}
