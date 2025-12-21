package com.example.bka.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bka.Constans.Role;
import com.example.bka.Models.Booking;
import com.example.bka.Models.User;
import com.example.bka.Repository.BookingRepository;
import com.example.bka.Repository.ScheduleRepository;
import com.example.bka.Repository.UserRepository;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    public Booking saveBooking(User user, Long scheduleId, Integer ticketCount) {

        Booking booking = new Booking();

        booking.setUser(user);
        booking.setSchedule(scheduleRepository.findById(scheduleId).orElse(null));
        booking.setTicketCount(ticketCount);
        booking.setTotalPayment(booking.getSchedule().getRoute().getBasePrice() * ticketCount);

        return bookingRepository.save(booking);
    }
    public Booking getBooking(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }
    public Booking getBookingByUser(User user) {
        return bookingRepository.findByUser(user);
    }
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
    public void deleteBookingBuy(Long id,User user,Booking booking) {
        user.setBalance(user.getBalance() + booking.getTotalPayment());

        User userAdmin = userRepository.findByRole(Role.ADMIN);
        userAdmin.setBalance(userAdmin.getBalance() - booking.getTotalPayment());
        userRepository.save(userAdmin);
        userRepository.save(user);
        bookingRepository.deleteById(id);  
    }
    
    public void buyBooking(User user, Booking booking) {
        if (user.getBalance() < booking.getTotalPayment()) {
            bookingRepository.delete(booking);
            throw new IllegalArgumentException("Saldo tidak cukup");
        }
        user.setBalance(user.getBalance() - booking.getTotalPayment());

        User userAdmin = userRepository.findByRole(Role.ADMIN);
        userAdmin.setBalance(userAdmin.getBalance() + booking.getTotalPayment());
        userRepository.save(user);
        userRepository.save(userAdmin);
    }
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}
