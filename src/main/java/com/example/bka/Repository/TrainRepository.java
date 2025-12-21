package com.example.bka.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bka.Models.Train;

public interface TrainRepository extends JpaRepository<Train, Long> {
    Train findByTrainCode(String trainCode);
    Train findByTrainName(String trainName);
}
