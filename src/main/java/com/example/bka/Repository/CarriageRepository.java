package com.example.bka.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bka.Constans.CarriageType;
import com.example.bka.Models.Carriage;
import com.example.bka.Models.Train;

public interface CarriageRepository extends JpaRepository<Carriage, Long> {

     boolean existsByTrainAndCarriageNumber(Train train,String carriageNumber);

     boolean existsByTrain_IdAndCarriageNumberAndIdNot(Long trainId,String carriageNumber,Long id);
     

    List<Carriage> findByTrain(Train train);

    List<Carriage> findByTypeAndTrain(CarriageType type, Train train);
    
}
