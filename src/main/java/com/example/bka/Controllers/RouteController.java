package com.example.bka.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.bka.Constans.Role;
import com.example.bka.Models.Route;
import com.example.bka.Models.User;
import com.example.bka.Services.RouteService;
import com.example.bka.Services.StationService;

import jakarta.servlet.http.HttpSession;


@Controller
public class RouteController {

    @Autowired
    private RouteService routeService;

    @Autowired
    private StationService stationService;

    @GetMapping("/route")
    public String route(Model model, HttpSession session) {
         User user = (User) session.getAttribute("userLogin");
        if (user == null || user.getRole().equals(Role.USER)) {
            return "redirect:/";
        }
        model.addAttribute("routes", routeService.getAllRoutes());
        
        model.addAttribute("stationByCity", stationService.getStationsGroupedByCity());
        
        model.addAttribute("route", new Route());
        
        model.addAttribute("mode", "add");
        return "route";
    }
    
    @PostMapping("/route")
    public String routeSave(@ModelAttribute("route") Route route, Model model) {
        try{
            routeService.saveRoute(route);
            return "redirect:/route";
        }catch(IllegalArgumentException e){
            model.addAttribute("routes", routeService.getAllRoutes());
            model.addAttribute("stationByCity", stationService.getStationsGroupedByCity());
            model.addAttribute("error", e.getMessage());
            return "route";
        }
    }
    
    @GetMapping("/route/{id}")
    public String routeDelete(@PathVariable(value = "id") long id,Model model) {
        try {
            routeService.deleteRoute(id);
            return "redirect:/route";
            
        } catch (IllegalArgumentException e) {
            model.addAttribute("routes", routeService.getAllRoutes());
            model.addAttribute("stationByCity", stationService.getStationsGroupedByCity());
            model.addAttribute("route", new Route());
            model.addAttribute("mode", "add");
            model.addAttribute("error", e.getMessage());
            return "route";
        }
    }
    
    @GetMapping("/route/update/{id}")
    public String routeUpdate(@PathVariable(value = "id") long id,Model model) {
        model.addAttribute("routes", routeService.getAllRoutes());      
        model.addAttribute("stationByCity", stationService.getStationsGroupedByCity());
        model.addAttribute("route", routeService.getRoute(id));
        model.addAttribute("mode", "update");
        return "route";
    }
    
    @PostMapping("/route/update/{id}")
    public String routeUpdateSave(@PathVariable(value = "id") long id,@ModelAttribute("route") Route route,Model model) {
        try {
            route.setId(id);
            routeService.saveRoute(route);
            return "redirect:/route";
        } catch (IllegalArgumentException e) {
            model.addAttribute("routes", routeService.getAllRoutes());      
            model.addAttribute("stationByCity", stationService.getStationsGroupedByCity());
            model.addAttribute("route", routeService.getRoute(id));
            model.addAttribute("mode", "update");
            model.addAttribute("error", e.getMessage());
            return "route";
            
        }
    }


    


}
