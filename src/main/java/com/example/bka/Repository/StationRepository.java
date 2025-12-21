package com.example.bka.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bka.Models.Station;

public interface StationRepository extends JpaRepository<Station, Long> {
    Station findByStationCode(String stationCode);
    Station findByStationName(String stationName);
    
}
