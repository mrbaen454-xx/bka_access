package com.example.bka.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.bka.Constans.Role;
import com.example.bka.Models.Schedule;
import com.example.bka.Models.User;
import com.example.bka.Services.RouteService;
import com.example.bka.Services.ScheduleService;
import com.example.bka.Services.TrainService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ScheduleController {
    
    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private TrainService trainService;

    @GetMapping("/schedule")
    public String schedule(Model model, HttpSession session) 
    {
         User user = (User) session.getAttribute("userLogin");
        if (user == null || user.getRole().equals(Role.USER)) {
            return "redirect:/";
        }
        model.addAttribute("schedules", scheduleService.getAllSchedules());
        model.addAttribute("schedule",new Schedule());
        model.addAttribute("routes",routeService.getAllRoutes());
        model.addAttribute("trains",trainService.getAllTrains());
        model.addAttribute("mode", "add");
        return "schedule";
    }
    @PostMapping("/schedule")
    public String scheduleSave(@ModelAttribute("schedule") Schedule schedule, Model model)
    {
        try {
            scheduleService.saveSchedule(schedule);
            return "redirect:/schedule";
            
        } catch (IllegalArgumentException e) {
            model.addAttribute("routes",routeService.getAllRoutes());
            model.addAttribute("trains",trainService.getAllTrains());
            model.addAttribute("schedules", scheduleService.getAllSchedules());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("mode", "add");
            return "schedule";
        }

    }
    @GetMapping("/schedule/update/{id}")
    public String scheduleUpdate(@PathVariable(value = "id") long id, Model model) {
        model.addAttribute("schedules", scheduleService.getAllSchedules());
        model.addAttribute("schedule", scheduleService.getSchedule(id));
        model.addAttribute("routes",routeService.getAllRoutes());
        model.addAttribute("trains",trainService.getAllTrains());
        model.addAttribute("mode", "update");
        return "schedule";
    }
    @PostMapping("/schedule/update/{id}")
    public String scheduleUpdateSave(@PathVariable(value = "id") long id, @ModelAttribute("schedule") Schedule schedule, Model model) {
        try {
            schedule.setId(id);
            scheduleService.saveSchedule(schedule);
            return "redirect:/schedule";
            
        } catch (IllegalArgumentException e) {
            model.addAttribute("schedules", scheduleService.getAllSchedules());
            model.addAttribute("schedule", scheduleService.getSchedule(id));
            model.addAttribute("routes",routeService.getAllRoutes());
            model.addAttribute("trains",trainService.getAllTrains());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("mode", "update");
            return "schedule";
        }
    }

    @GetMapping("/schedule/delete/{id}")
    public String scheduleDelete(@PathVariable(value = "id") long id,Model model) {
        try {
            
            scheduleService.deleteSchedule(id);
            return "redirect:/schedule";
        } catch (IllegalArgumentException e) {
            model.addAttribute("schedules", scheduleService.getAllSchedules());
            model.addAttribute("schedule", new Schedule());
            model.addAttribute("routes", routeService.getAllRoutes());
            model.addAttribute("trains", trainService.getAllTrains());
            model.addAttribute("mode", "add");
            model.addAttribute("error", e.getMessage());
            return "schedule";
        }
    }

}
