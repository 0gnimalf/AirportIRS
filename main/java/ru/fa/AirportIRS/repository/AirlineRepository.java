package ru.fa.AirportIRS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fa.AirportIRS.models.Airline;

@Repository
public interface AirlineRepository extends JpaRepository<Airline, String> {
}
