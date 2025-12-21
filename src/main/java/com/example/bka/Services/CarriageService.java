package com.example.bka.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bka.Constans.CarriageType;
import com.example.bka.Models.Carriage;
import com.example.bka.Models.Seat;
import com.example.bka.Models.Train;
import com.example.bka.Repository.CarriageRepository;
import com.example.bka.Repository.SeatRepository;

@Service
public class CarriageService {

    @Autowired
    private CarriageRepository carriageRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private TrainService trainService;

    public Carriage addCarriage(Carriage carriage) {
        carriage.setId(null);
        if (carriageRepository.existsByTrainAndCarriageNumber(
                carriage.getTrain(),
                carriage.getCarriageNumber())) {

            throw new IllegalArgumentException(
                    "Nomor gerbong sudah ada di kereta ini");
        }
        if (carriage.getSeatCount() < 10) {
            throw new IllegalArgumentException("Jumlah kursi tidak valid");
        }

        Carriage savedCarriage = carriageRepository.save(carriage);

        saveSeat(carriage.getSeatCount(), savedCarriage);
        return savedCarriage;

    }

    public void saveSeat(int totalSeats, Carriage savedCarriage) {
        char[] columns = { 'A', 'B', 'C', 'D' };
        int seatsPerRow = columns.length;
        int rows = (int) Math.ceil((double) totalSeats / seatsPerRow);

        int seatNumberCounter = 0;

        for (int i = 1; i <= rows; i++) {
            for (char c : columns) {
                if (seatNumberCounter >= totalSeats) {
                    break;
                }

                Seat seat = new Seat();
                seat.setSeatNumber(i + "" + c);
                seat.setCarriage(savedCarriage);
                seatRepository.save(seat);
                seatNumberCounter++;
            }
        }

    }

    public Carriage updateCarriage(Long id, Long trainId, Carriage carriage) {

        Carriage existingCarriage = carriageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Carriage tidak ditemukan"));

        List<Seat> existingSeats = seatRepository.findByCarriage(existingCarriage);
        for (Seat seat : existingSeats) {
            if (seat.getSeatBooking() != null && !seat.getSeatBooking().isEmpty()) {
                throw new IllegalArgumentException(
                        "Carriage tidak dapat diupdate karena sudah digunakan dalam booking");
            }
        }
        carriage.setId(id);
        carriage.setTrain(trainService.getTrain(trainId));
        if (carriageRepository.existsByTrain_IdAndCarriageNumberAndIdNot(trainId, carriage.getCarriageNumber(), id)) {

            throw new IllegalArgumentException(
                    "Nomor gerbong sudah ada di kereta ini");
        }
        if (carriage.getSeatCount() < 10) {
            throw new IllegalArgumentException("Jumlah kursi tidak valid");
        }
        Carriage savedCarriage = carriageRepository.save(carriage);

        List<Seat> seats = seatRepository.findByCarriage(savedCarriage);
        seatRepository.deleteAll(seats);

        saveSeat(carriage.getSeatCount(), savedCarriage);

        return savedCarriage;

    }

    public List<Carriage> getAllByTrain(Train train) {
        return carriageRepository.findByTrain(train);
    }

    public Carriage getCarriage(Long id) {
        return carriageRepository.findById(id).orElse(null);
    }

    public void deleteCarriage(Long id) {
        Carriage carriage = carriageRepository.findById(id).orElseThrow();
       
        for (Seat seat : carriage.getSeats()) {
            if (seat.getSeatBooking() != null && !seat.getSeatBooking().isEmpty()) {
                throw new IllegalArgumentException(
                        "Tidak dapat menghapus carriage karena ada seat yang sudah dibooking");
            }
        }
        seatRepository.deleteAll(carriage.getSeats());
        carriageRepository.deleteById(id);
    }

    public List<Carriage> findByTypeAndTrain(CarriageType type, Train train) {
        List<Carriage> carriages = carriageRepository.findByTypeAndTrain(type, train);
        for (Carriage carriage : carriages) {
            if (carriage.getSeats().isEmpty()) {
                throw new IllegalArgumentException("Tidak ada kursi di gerbong ini");
            }
        }
        
        return carriages;
    }
}
