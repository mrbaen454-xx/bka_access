package com.example.bka.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bka.Models.Carriage;
import com.example.bka.Models.Seat;
import com.example.bka.Repository.SeatRepository;

@Service
public class SeatService {
    
    @Autowired
    private SeatRepository seatRepository;

    public List<Seat> getByCarriage(Carriage carriage) {
        return seatRepository.findByCarriage(carriage);
    }
    public Seat getSeat(Long id) {
        return seatRepository.findById(id).orElse(null);
    }
}
