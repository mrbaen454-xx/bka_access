package com.example.bka.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bka.Models.Schedule;
import com.example.bka.Models.Train;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByTrainOrderByArrivalTimeDesc(Train train);

    List<Schedule> findByArrivalTimeBefore(LocalDateTime time);
    
    List<Schedule> findByRoute_DepartureStation_StationNameContainingAndRoute_ArrivalStation_StationNameContainingAndDepartureTimeAfter(
            String departureStation,
            String arrivalStation,
            LocalDateTime time);

}
