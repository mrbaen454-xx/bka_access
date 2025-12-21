package com.example.bka.Models;



import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
// @Data@Getter
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "departure_station_id")
    private Station departureStation;
    @ManyToOne
    @JoinColumn(name = "arrival_station_id")
    private Station arrivalStation;
    private Double distance;
    private Double basePrice;
    private Boolean active;

     @OneToMany(
        mappedBy = "route"
    )
    private List<Schedule> schedules;

    
}
