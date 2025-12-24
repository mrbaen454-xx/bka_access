package com.example.bka.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bka.Models.Booking;
import com.example.bka.Models.Seat;
import com.example.bka.Models.SeatBooking;
import com.example.bka.Repository.BookingRepository;
import com.example.bka.Repository.SeatBookingRepository;
import com.example.bka.Repository.SeatRepository;

@Service
public class SeatBookingService {

    @Autowired
    private SeatBookingRepository seatBookingRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public SeatBooking savSeatBooking(Booking booking, Long seatId) {
        Seat seat = seatRepository.findById(seatId).orElseThrow(() -> new IllegalArgumentException("Seat tidak ditemukan"));

        SeatBooking sb = new SeatBooking();
        sb.setBooking(booking);
        sb.setSeat(seat);

        seat.setBooked(true);

        return seatBookingRepository.save(sb);
    }

    public List<SeatBooking> getByBooking(List<Booking> booking) {
        if (booking == null || booking.isEmpty()) {
            throw new IllegalArgumentException("Anda belum punya ticktet");
        }
        
        List<SeatBooking> result = new ArrayList<>();
        for (Booking b : booking) {
            result.addAll(seatBookingRepository.findByBooking(b));
        }
        return result;
    }


    public void deleteSeatBooking(Long id) {
        SeatBooking sb = seatBookingRepository.findById(id).orElseThrow();
        Booking booking = sb.getBooking();
        if (LocalDateTime.now().isAfter(booking.getSchedule().getDepartureTime().minusMinutes(60))) {
            throw new IllegalArgumentException("Tidak dapat membatalkan pemesanan");
        }
        if (booking.getSeatBooking() == null) {
            bookingRepository.delete(booking);
        }

        Seat seat = sb.getSeat();
        seat.setBooked(false);
        
        seatBookingRepository.deleteById(id);
    }
    public List<SeatBooking> getAllSeatBooking() {
        if (seatBookingRepository.findAll().isEmpty()) {
            throw new IllegalArgumentException("Tidak ada tiket");
            
        }
        return seatBookingRepository.findAll();
    }
}

