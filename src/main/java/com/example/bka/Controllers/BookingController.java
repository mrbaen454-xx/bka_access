package com.example.bka.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.bka.Constans.Role;
import com.example.bka.Models.Booking;
import com.example.bka.Models.User;
import com.example.bka.Services.BookingService;
import com.example.bka.Services.ScheduleService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/booking/{id}")
    public String booking(@PathVariable(value = "id") long id, Model model, HttpSession session) {
         User user = (User) session.getAttribute("userLogin");
        if (user == null || user.getRole().equals(Role.ADMIN)) {
            return "redirect:/";
        }
        Booking booking = new Booking();
        model.addAttribute("booking", booking);
        model.addAttribute("totalPayment", booking.getTotalPayment());
        model.addAttribute("schedule", scheduleService.getSchedule(id));
        Boolean showBuyButton = (Boolean) session.getAttribute("showBuyButton");
        showBuyButton = false;
        model.addAttribute("showBuyButton", showBuyButton);

        return "booking";
    }

    @PostMapping("/booking/{id}")
    public String saveBooking(@PathVariable("id") long id,
            HttpSession session,
            @RequestParam("ticketCount") Integer ticketCount,
            Model model) {
        try {
            User user = (User) session.getAttribute("userLogin");
            Booking booking = bookingService.saveBooking(user, id, ticketCount);

            // simpan di session supaya bisa dipakai saat buy
            session.setAttribute("booking", booking);

            // tambahkan semua atribut ke model agar Thymeleaf aman
            model.addAttribute("booking", booking);
            model.addAttribute("schedule", booking.getSchedule());
            model.addAttribute("totalPayment", booking.getTotalPayment());

            session.setAttribute("showBuyButton", true);
            Boolean showBuyButton = (Boolean) session.getAttribute("showBuyButton");
            model.addAttribute("showBuyButton", showBuyButton);

            return "booking"; // tetap di halaman booking, tidak redirect
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());

            // tetap tambahkan schedule biar template tidak error
            model.addAttribute("schedule", scheduleService.getSchedule(id));
            return "booking";
        }
    }

    @PostMapping("/booking/buy")
    public String buyBooking(HttpSession session, Model model) {
        try {
            Booking booking = (Booking) session.getAttribute("booking");
            User user = (User) session.getAttribute("userLogin");
            bookingService.buyBooking(user, booking);
            session.setAttribute("showBuyButton", false);

            return "redirect:/seatBooking/" + booking.getId();
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            Booking booking = (Booking) session.getAttribute("booking");
            model.addAttribute("booking", booking);
            model.addAttribute("schedule", booking.getSchedule());
            model.addAttribute("totalPayment", booking.getTotalPayment());
            return "booking";
        }
    }

    @GetMapping("/booking/delete/{id}")
    public String deleteBooking(@PathVariable(value = "id") long id) {

        bookingService.deleteBooking(id);
        return "redirect:/user";
    }

    @GetMapping("/booking/delete/buy/{id}")
    public String deleteBookingBuy(@PathVariable(value = "id") long id, HttpSession session) {
        User user = (User) session.getAttribute("userLogin");
        Booking booking = bookingService.getBooking(id);
        bookingService.deleteBookingBuy(id, user, booking);
        return "redirect:/user";
    }

}
