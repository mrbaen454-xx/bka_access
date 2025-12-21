package com.example.bka.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bka.Models.Carriage;
import com.example.bka.Models.Seat;

public interface SeatRepository extends JpaRepository<Seat, Long> {
   List<Seat> findByCarriage(Carriage carriage);


}
