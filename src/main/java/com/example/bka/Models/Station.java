package com.example.bka.Models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String stationName;
    private String stationCode;
    private String city;
    private Boolean active;

    @OneToMany(
        mappedBy = "departureStation"
    )
    private List<Route> routes;

    @OneToMany(
        mappedBy = "arrivalStation"
    )
    private List<Route> routes2;
    
    
}
