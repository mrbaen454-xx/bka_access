package com.example.bka.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bka.Models.Route;
import com.example.bka.Repository.RouteRepository;

@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;

    public Route saveRoute(Route route) {
        List<Route> checkRoute = routeRepository.findAll();

        for (Route r : checkRoute) 
        {
            if ( r.getDepartureStation().getId() == route.getDepartureStation().getId() &&
                r.getArrivalStation().getId() == route.getArrivalStation().getId() && r.getId() != route.getId()) 
            {
                throw new IllegalArgumentException("Rute bentrok dengan rute lain!");
            }

        }
        if (route.getDepartureStation().getId() == route.getArrivalStation().getId()) {
            throw new IllegalArgumentException("Stasiun keberangkatan dan kedatangan tidak boleh sama");
        }
        if (route.getBasePrice() <= 10000 || route.getDistance() < 0) {
            throw new IllegalArgumentException("Harga atau jarak tidak valid");
        }
        if (route.getDepartureStation().getActive() == false || route.getArrivalStation().getActive() == false) {
            throw new IllegalArgumentException("Rute harus aktif");
        }

        return routeRepository.save(route);
    }

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    public void deleteRoute(long id) {
        Route route = routeRepository.findById(id).orElse(null);
        if (!route.getSchedules().isEmpty()) {
            throw new IllegalArgumentException("Tidak bisa menghapus Route yang sudah memiliki Schedule");
        }
        routeRepository.deleteById(id);
    }
    public Route getRoute(long id) {
        return routeRepository.findById(id).orElse(null);
    }


}

