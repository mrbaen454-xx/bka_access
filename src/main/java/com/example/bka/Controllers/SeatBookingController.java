package com.example.bka.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.bka.Constans.CarriageType;
import com.example.bka.Constans.Role;
import com.example.bka.Models.Booking;
import com.example.bka.Models.Carriage;
import com.example.bka.Models.Seat;
import com.example.bka.Models.Train;
import com.example.bka.Models.User;
import com.example.bka.Services.BookingService;
import com.example.bka.Services.CarriageService;
import com.example.bka.Services.SeatBookingService;
import com.example.bka.Services.SeatService;

import jakarta.servlet.http.HttpSession;

@Controller
public class SeatBookingController {

    @Autowired
    private CarriageService carriageService;

    @Autowired
    private SeatService seatService;

    @Autowired
    private SeatBookingService seatBookingService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/seatBooking/{id}")
    public String seatBooking(@PathVariable(value = "id") Long id,
            @RequestParam(value = "typeCarriage", required = false) CarriageType typeCarriage, Model model, HttpSession session) {
        try {
            
            User user = (User) session.getAttribute("userLogin");
            if (user == null || user.getRole().equals(Role.ADMIN)) {
                return "redirect:/";
        }
        Booking booking = bookingService.getBooking(id);
        Train train = booking.getSchedule().getTrain();
        
        List<Carriage> carriages = carriageService.findByTypeAndTrain(typeCarriage, train);
        List<Seat> seats = new ArrayList<>();
        for (Carriage carriage : carriages) {
            seats.addAll(seatService.getByCarriage(carriage));
        }
        model.addAttribute("train", train);
        model.addAttribute("types", CarriageType.values());
        model.addAttribute("seats", seats);
        model.addAttribute("booking", booking);
        if (seats.isEmpty()) {
            
            model.addAttribute("typeCarriage",false);
        }else{

            model.addAttribute("typeCarriage", typeCarriage);
        }
        
        return "seatBooking";
    } catch (IllegalArgumentException e) {
        model.addAttribute("types", CarriageType.values());
        Booking booking = bookingService.getBooking(id);
        model.addAttribute("booking", booking);
        model.addAttribute("typeCarriage", false);
        model.addAttribute("error", e.getMessage());
        return "seatBooking";
    }
    }

    @PostMapping("/seatBooking")
    public String seatBookingSave(@RequestParam(value = "seatId") List<Long> seatId,
            @RequestParam(value = "bookingId") Long bookingId, Model model) {
        try {
            Booking booking = bookingService.getBooking(bookingId);
            if (seatId.size() != booking.getTicketCount()) {
                throw new IllegalArgumentException("Harus memilih seat sesuai jumlah tiket");
            }
            for (Long seat : seatId) {
                seatBookingService.savSeatBooking(booking, seat);
            }

            return "redirect:/ticket";

        } catch (IllegalArgumentException e) {
            model.addAttribute("types", CarriageType.values());
            Booking booking = bookingService.getBooking(bookingId);
            model.addAttribute("booking", booking);
            model.addAttribute("typeCarriage", false);
            model.addAttribute("error", e.getMessage());
            return "seatBooking";
        }
    }

    @GetMapping("/ticket")
    public String ticket(Model model, HttpSession session) {
        try {

            User user = (User) session.getAttribute("userLogin");
            List<Booking> booking = bookingService.getBookingByUser(user);
            model.addAttribute("tickets", seatBookingService.getByBooking(booking));
            return "ticket";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "ticket";
        }
    }

    @GetMapping("/ticket/delete/{id}")
    public String ticketDelete(@PathVariable(value = "id") Long id, Model model) {
        try {
            seatBookingService.deleteSeatBooking(id);
            return "redirect:/ticket";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "ticket";
        }
    }

}
