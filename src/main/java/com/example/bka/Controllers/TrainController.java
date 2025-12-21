package com.example.bka.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.bka.Constans.Role;
import com.example.bka.Models.Train;
import com.example.bka.Models.User;
import com.example.bka.Services.TrainService;

import jakarta.servlet.http.HttpSession;

@Controller
public class TrainController {

    @Autowired
    private TrainService trainService;

   @GetMapping("/train")
public String train(Model model,HttpSession session,@RequestParam(value = "dir", required = false, defaultValue = "asc") String dir) {
    User user = (User) session.getAttribute("userLogin");
    if (user == null || user.getRole().equals(Role.USER)) {
        return "redirect:/";
    }

    model.addAttribute("trains", trainService.getAllTrainsSorted(dir));
    model.addAttribute("train", new Train());
    model.addAttribute("mode", "add");

    model.addAttribute("currentDir", dir);
    model.addAttribute("nextDir", dir.equals("asc") ? "desc" : "asc");

    return "train";
}

    
    @PostMapping("/train")
    public String saveTrain(@ModelAttribute("train") Train train, Model model) {
        try {
            trainService.saveTrain(train);            
            return "redirect:/train";
            
        } catch (IllegalArgumentException e) {
            model.addAttribute("trains", trainService.getAllTrains());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("mode", "add");
            return "train";
        }
    }
    @GetMapping("/train/update/{id}")
    public String updateTrain(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("trains", trainService.getAllTrains());
        model.addAttribute("train", trainService.getTrain(id));
        model.addAttribute("mode", "update");
        return "train";
    }
    @PostMapping("/train/update/{id}")
    public String updateTrainSave(@PathVariable(value = "id") Long id, @ModelAttribute("train") Train train, Model model) {
        try {
            train.setId(id);
            trainService.saveTrain(train);
            return "redirect:/train";
            
        } catch (IllegalArgumentException e) {
            model.addAttribute("trains", trainService.getAllTrains());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("mode", "update");
            return "train";
        }
    }
    @GetMapping("/train/delete/{id}")
    public String deleteTrain(@PathVariable(value = "id") Long id,Model model) {
        try {
            trainService.deleteTrain(id);
            return "redirect:/train";
            
        } catch (IllegalArgumentException e) {
            model.addAttribute("trains", trainService.getAllTrains());
            model.addAttribute("train", new Train());
            model.addAttribute("mode", "add");
            model.addAttribute("error", e.getMessage());
            return "train";
        }
    }
}
