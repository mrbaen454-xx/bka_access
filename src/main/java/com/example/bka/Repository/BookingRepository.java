package com.example.bka.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bka.Models.Booking;
import com.example.bka.Models.Schedule;
import com.example.bka.Models.User;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBySchedule(Schedule schedule);
    void deleteBySchedule(Schedule schedule);
   List<Booking> findByUser(User user);
}
