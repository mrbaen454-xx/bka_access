package com.example.bka.Models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
public class Train {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String trainCode;
    private String trainName;
    private Boolean active = true;

       @OneToMany(
        mappedBy = "train"
    )
    @ToString.Exclude
    private List<Carriage> carriages;

    @OneToMany(
        mappedBy = "train"
    )
    // @ToString.Exclude
    private List<Schedule> schedules;
}
