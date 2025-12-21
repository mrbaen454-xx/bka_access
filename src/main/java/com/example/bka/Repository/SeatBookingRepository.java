package com.example.bka.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bka.Models.Booking;
import com.example.bka.Models.SeatBooking;

public interface SeatBookingRepository extends JpaRepository<SeatBooking,Long>{

    void deleteByBooking(Booking booking);
    List<SeatBooking> findByBooking(Booking booking);
    
}
