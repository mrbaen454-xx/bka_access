package com.example.bka.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.bka.Models.Carriage;
import com.example.bka.Services.CarriageService;
import com.example.bka.Services.SeatService;


@Controller
public class SeatController {
    
    @Autowired
    private SeatService seatService;

    @Autowired
    private CarriageService carriageService;

    @GetMapping("/seat/{id}")
    public String seat(@PathVariable(value = "id") Long id, Model model) {
        Carriage carriage = carriageService.getCarriage(id);
        model.addAttribute("seats", seatService.getByCarriage(carriage));
        model.addAttribute("carriage", carriage);
        return "seat";
        
    }
}
