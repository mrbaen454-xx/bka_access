package com.example.bka.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.bka.Constans.CarriageType;
import com.example.bka.Models.Carriage;
import com.example.bka.Models.Train;
import com.example.bka.Services.CarriageService;
import com.example.bka.Services.TrainService;

@Controller
public class CarriageController {

    @Autowired
    private CarriageService carriageService;

    @Autowired
    private TrainService trainService;

    @GetMapping("/carriage/{id}")
    public String carriage(@PathVariable(value = "id") Long id, Model model) {
        Train train = trainService.getTrain(id);
        model.addAttribute("carriages", carriageService.getAllByTrain(train));
        model.addAttribute("carriage", new Carriage());
        model.addAttribute("train", train);
        model.addAttribute("types", CarriageType.values());
        model.addAttribute("mode", "add");
        return "carriage";
    }
    
    @PostMapping("/carriage/{id}")
    public String saveCarriage(@ModelAttribute("carriage") Carriage carriage, @PathVariable(value = "id") Long id, Model model) {
        try {
            Train train = trainService.getTrain(id);
            carriage.setTrain(train);
            carriageService.addCarriage(carriage);
            
            return "redirect:/carriage/" + id;
            
        } catch (IllegalArgumentException e) {
            Train train = trainService.getTrain(id);
            model.addAttribute("train", train);
            model.addAttribute("carriages", carriageService.getAllByTrain(train));
            model.addAttribute("types", CarriageType.values());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("mode", "add");
            return "carriage";
        }
    }
    @GetMapping("/carriage/update/{id}/{trainId}")
    public String updateCarriage(@PathVariable(value = "id") Long id,@PathVariable(value = "trainId") Long trainId, Model model) {
        Train train = trainService.getTrain(trainId);
        model.addAttribute("carriages", carriageService.getAllByTrain(train));
        model.addAttribute("train", train);
        model.addAttribute("carriage", carriageService.getCarriage(id));
        model.addAttribute("types", CarriageType.values());
        model.addAttribute("mode", "update");
        return "carriage";
    }
    @PostMapping("/carriage/update/{id}/{trainId}")
    public String updateCarriageSave(@PathVariable(value = "id") Long id,@PathVariable(value = "trainId") Long trainId, @ModelAttribute("carriage") Carriage carriage,Model model) {
        try {
            carriageService.updateCarriage(id,trainId,carriage);
            return "redirect:/carriage/" + trainId;
            
        } catch (IllegalArgumentException e) {
            Train train = trainService.getTrain(trainId);
            model.addAttribute("train", train);
            model.addAttribute("carriages", carriageService.getAllByTrain(train));
            model.addAttribute("carriage", carriageService.getCarriage(id));
            model.addAttribute("types", CarriageType.values());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("mode", "update");
            return "carriage";

          
        }
    }
    @GetMapping("/carriage/delete/{id}/{trainId}")
    public String deleteCarriage(@PathVariable(value = "id") Long id, @PathVariable(value = "trainId") Long trainId, Model model) {
        try{
            carriageService.deleteCarriage(id);
            return "redirect:/carriage/" + trainId;

        }catch(IllegalArgumentException e){
            Train train = trainService.getTrain(trainId);
            model.addAttribute("train", train);
            model.addAttribute("carriages", carriageService.getAllByTrain(train));
            model.addAttribute("carriage", carriageService.getCarriage(id));
            model.addAttribute("types", CarriageType.values());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("mode", "add");
            return "carriage";
            
        }
    }
}
