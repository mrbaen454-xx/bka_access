package com.example.bka.Models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Seat {
     @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String seatNumber;

    @ManyToOne
    @JoinColumn(name = "carriage_id")
    private Carriage carriage;

    @OneToMany(mappedBy = "seat")
    private List<SeatBooking> seatBooking;
    

    private boolean isBooked = false;


    
}
