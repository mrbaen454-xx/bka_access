package com.example.bka.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.bka.Constans.Role;
import com.example.bka.Models.Station;
import com.example.bka.Models.User;
import com.example.bka.Services.StationService;

import jakarta.servlet.http.HttpSession;

@Controller
public class StationController {

    @Autowired
    private StationService stationService;

    @GetMapping("/station")
    public String station(Model model, HttpSession session) 
    {
         User user = (User) session.getAttribute("userLogin");
        if (user == null || user.getRole().equals(Role.USER)) {
            return "redirect:/";
        }
        model.addAttribute("stationByCity",stationService.getStationsGroupedByCity());
        model.addAttribute("station", new Station());
        model.addAttribute("mode", "add");
        return "station";
    }
    @PostMapping("/station")
    public String stationSave(@ModelAttribute("station") Station station,Model model) 
    {
        try {
               
            stationService.saveStasiun(station);
            return "redirect:/station";
            
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("stationByCity",stationService.getStationsGroupedByCity());
            return "station";
        }

    }

    @GetMapping("/station/update/{id}")
    public String stationUpdate(@PathVariable(value = "id") long id,Model model)
    {
        model.addAttribute("stationByCity",stationService.getStationsGroupedByCity());
        model.addAttribute("station", stationService.getStation(id));
        model.addAttribute("mode", "update");
        return "station";
    }
    @PostMapping("/station/update/{id}")
    public String stationUpdateSave(@PathVariable(value = "id") long id,@ModelAttribute("station") Station station,Model model)
    {
        try {
            station.setId(id);
            stationService.saveStasiun(station);
            return "redirect:/station";
            
        } catch (IllegalArgumentException e) {
            model.addAttribute("stationByCity",stationService.getStationsGroupedByCity());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("mode", "update");
            return "station";
        }
    }

    @GetMapping("/station/{id}")
    public String stationDelete(@PathVariable(value = "id") long id,Model model)
    {
        try {
            
            stationService.deletStation(id);
            return "redirect:/station";
        } catch (IllegalArgumentException e) {
            model.addAttribute("stationByCity", stationService.getStationsGroupedByCity());
            model.addAttribute("station", new Station());
            model.addAttribute("mode", "add");
            model.addAttribute("error", e.getMessage());
            return "station";
        }
    }

 
}
