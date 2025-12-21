package com.example.bka.Services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bka.Models.Station;
import com.example.bka.Repository.StationRepository;

@Service
public class StationService {

    @Autowired
    private StationRepository stationRepository;

    public Map<String, List<Station>> getStationsGroupedByCity() {

        List<Station> stations = stationRepository.findAll();

        return stations.stream().collect(Collectors.groupingBy(Station::getCity));
    }
    public Station getStation(long id) {
        Station station = stationRepository.findById(id).orElse(null);
        if(station != null) {
            return station;
        }
        throw new IllegalArgumentException("Stasiun tidak ditemukan");
    }
   
    public Station saveStasiun(Station station) {

        Station byCode = stationRepository.findByStationCode(station.getStationCode());
        if (byCode != null && byCode.getId() != station.getId()) {
            throw new IllegalArgumentException("Station code sudah digunakan");
        }

        Station byName = stationRepository.findByStationName(station.getStationName());
        if (byName != null && byName.getId() != station.getId()) {
            throw new IllegalArgumentException("Station name sudah digunakan");
        }

        return stationRepository.save(station);
    }

    public void deletStation(long id)
    {
        Station station = getStation(id);
        if (!station.getRoutes().isEmpty() || !station.getRoutes2().isEmpty()) {
            throw new IllegalArgumentException("Tidak dapat menghapus stasiun yang sudah memiliki rute");
        }
        stationRepository.deleteById(id);
    }

}
