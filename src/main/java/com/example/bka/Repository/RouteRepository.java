package com.example.bka.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bka.Models.Route;

public interface RouteRepository extends JpaRepository<Route, Long> {
    
}
