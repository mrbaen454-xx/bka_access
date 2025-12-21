package com.example.bka.Models;

import java.util.List;

import com.example.bka.Constans.CarriageType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.ToString;


@Entity
@Data
public class Carriage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String carriageNumber;
    @Enumerated(EnumType.STRING)
    private CarriageType type;

    private Integer seatCount;

    @ManyToOne
    @JoinColumn(name = "train_id")
    private Train train;


    @OneToMany(
        mappedBy = "carriage"
    )
    @ToString.Exclude
    private List<Seat> seats;
}
