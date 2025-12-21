package com.example.bka.Services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.bka.Models.Booking;
import com.example.bka.Models.Schedule;
import com.example.bka.Models.Seat;
import com.example.bka.Models.SeatBooking;
import com.example.bka.Models.Station;
import com.example.bka.Repository.BookingRepository;
import com.example.bka.Repository.ScheduleRepository;
import com.example.bka.Repository.SeatBookingRepository;

import jakarta.transaction.Transactional;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SeatBookingRepository seatBookingRepository;

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }


    public Schedule saveSchedule(Schedule schedule) {
        
      
        // =====================
        // VALIDASI DASAR
        // =====================
        if (!schedule.getDepartureTime().isBefore(schedule.getArrivalTime())) {
            throw new IllegalArgumentException(
                    "Waktu keberangkatan harus sebelum waktu kedatangan");
        }

        if (schedule.getDepartureTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Waktu keberangkatan tidak boleh di masa lalu");
        }
        if(schedule.getTrain().getCarriages().isEmpty()){
            throw new IllegalArgumentException("Kereta harus memiliki gerbong");
        }

        if (!schedule.getTrain().getActive()) {
            throw new IllegalArgumentException("Kereta harus aktif");
        }

        if (!schedule.getRoute().getActive()) {
            throw new IllegalArgumentException("Rute harus aktif");
        }

        // =====================
        // VALIDASI URUTAN RUTE PER KERETA
        // =====================
        List<Schedule> trainSchedules = scheduleRepository.findByTrainOrderByArrivalTimeDesc(schedule.getTrain());

        if (!trainSchedules.isEmpty()) {
            Schedule lastSchedule = trainSchedules.get(0);
            Station lastArrivalStation = lastSchedule.getRoute().getArrivalStation();
            Station newDepartureStation = schedule.getRoute().getDepartureStation();

            if ((lastArrivalStation.getId() != newDepartureStation.getId()) && schedule.getId() != lastSchedule.getId()) {
                throw new IllegalArgumentException(
                        "Rute tidak valid. Kereta terakhir tiba di stasiun "
                                + lastArrivalStation.getStationName()
                                + ", rute baru harus berangkat dari stasiun tersebut");
            }

            LocalDateTime minimalDeparture = lastSchedule.getArrivalTime().plusMinutes(20);
            if ((!schedule.getDepartureTime().isAfter(minimalDeparture)) && schedule.getId() != lastSchedule.getId()) {
                throw new IllegalArgumentException(
                        "Kereta membutuhkan jeda minimal 20 menit setelah jadwal sebelumnya. "
                                + "Jadwal berikutnya harus setelah " + minimalDeparture);
            }
        }

        // =====================
        //VALIDASI JEDA DI STASIUN (DEPARTURE & ARRIVAL) UNTUK SEMUA KERETA
        // =====================
        List<Schedule> allSchedules = scheduleRepository.findAll();
        if (!allSchedules.isEmpty()) {
            
            for (Schedule existing : allSchedules) {
                
            LocalDateTime existingDep = existing.getDepartureTime();
            LocalDateTime existingArr = existing.getArrivalTime();

            LocalDateTime newDep = schedule.getDepartureTime();
            LocalDateTime newArr = schedule.getArrivalTime();

            // CEK JEDA 20 MENIT DI STASIUN KEBERANGKATAN
            if (existing.getRoute().getDepartureStation().getId()
                    == (schedule.getRoute().getDepartureStation().getId())
                    || existing.getRoute().getArrivalStation().getId()
                            ==(schedule.getRoute().getDepartureStation().getId())) {
//                    10:00           11:20                            10:00               9:40
                if ((newDep.isBefore(existingArr.plusMinutes(20)) && newDep.isAfter(existingDep.minusMinutes(20))) && schedule.getId() != existing.getId()) {
                    throw new IllegalArgumentException(
                            "Keberangkatan kereta baru di stasiun "
                                    + schedule.getRoute().getDepartureStation().getStationName()
                                    + " bentrok dengan kereta lain. Harus ada jeda 20 menit.");
                }
            }

            // CEK JEDA 20 MENIT DI STASIUN TUJUAN
            if ((existing.getRoute().getDepartureStation().getId()
                    == (schedule.getRoute().getArrivalStation().getId())) && schedule.getId() != existing.getId()) {
//                          11:00               11:20                        11:00                9:40
                if (newArr.isBefore(existingDep.plusMinutes(20))) {
                    throw new IllegalArgumentException(
                            "Kedatangan kereta baru di stasiun "
                                    + schedule.getRoute().getArrivalStation().getStationName()
                                    + " bentrok dengan kereta lain. Harus ada jeda 20 menit.");
                }
            }
            
        }
        List<Booking> bookings = bookingRepository.findBySchedule(schedule);
        for(Booking b : bookings)
            {
                if(b.getSchedule().getId() == schedule.getId()){
                    throw new IllegalArgumentException("Schedule tidak bisa di update karena sudah memiliki booking");
                }
            }
        }

      

        return scheduleRepository.save(schedule);
    }
    public Schedule getSchedule(long id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void delete(){
        LocalDateTime time = LocalDateTime.now();

        List<Schedule> schedules = scheduleRepository.findByArrivalTimeBefore(time);

        for (Schedule s : schedules) {

            List<Booking> bookings = bookingRepository.findBySchedule(s);

            for (Booking b : bookings) {

                List<SeatBooking> seatBookings = seatBookingRepository.findByBooking(b);
                
                for (SeatBooking sb : seatBookings) {
                    Seat seat = sb.getSeat();
                    seat.setBooked(false);

                }
                seatBookingRepository.deleteByBooking(b);
                
            }

            bookingRepository.deleteBySchedule(s);
           
            scheduleRepository.delete(s);
        }

        

    }
  
    public void deleteSchedule(long id) {

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule tidak ditemukan"));

        if (!schedule.getBooking().isEmpty()) {
            throw new IllegalArgumentException(
                    "Tidak dapat menghapus schedule yang sudah memiliki booking");
        }

        scheduleRepository.delete(schedule);
    }

    public List<Schedule> serch(String departureStation, String arrivalStation) {
        return scheduleRepository.findByRoute_DepartureStation_StationNameContainingAndRoute_ArrivalStation_StationNameContainingAndDepartureTimeAfter(departureStation, arrivalStation, LocalDateTime.now());
    }
}
