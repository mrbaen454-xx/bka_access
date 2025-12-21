package com.example.bka.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bka.Models.Carriage;
import com.example.bka.Models.Train;
import com.example.bka.Repository.CarriageRepository;
import com.example.bka.Repository.SeatRepository;
import com.example.bka.Repository.TrainRepository;
import org.springframework.data.domain.Sort;

@Service
public class TrainService {

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private CarriageRepository carriageRepository;

    @Autowired
    private SeatRepository seatRepository;

    public Train saveTrain(Train train) {

        Train byCode = trainRepository.findByTrainCode(train.getTrainCode());
        if (byCode != null && byCode.getId() != train.getId()) {
            throw new IllegalArgumentException("Train code sudah digunakan");
        }
        Train byName = trainRepository.findByTrainName(train.getTrainName());
        if (byName != null && byName.getId() != train.getId()) {
            throw new IllegalArgumentException("Train name sudah digunakan");

        }

        return trainRepository.save(train);
    }

    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }

    public List<Train> getAllTrainsSorted(String direction) {

        Sort sort;

        if ("desc".equalsIgnoreCase(direction)) {
            sort = Sort.by("trainName").descending();
        } else {
            sort = Sort.by("trainName").ascending();
        }

        return trainRepository.findAll(sort);
    }

    public Train getTrain(long id) {
        return trainRepository.findById(id).orElse(null);
    }

    public void deleteTrain(long id) {
        Train train = trainRepository.findById(id).orElseThrow();

        if (!train.getSchedules().isEmpty()) {
            throw new IllegalArgumentException("Tidak dapat menghapus train yang sudah memiliki schedule");
        }

        for (Carriage c : train.getCarriages()) {

            seatRepository.deleteAll(c.getSeats());
        }
        carriageRepository.deleteAll(train.getCarriages());

        trainRepository.delete(train);
    }

}
