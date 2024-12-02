package ru.fa.AirportIRS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fa.AirportIRS.models.Aircraft;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, String> {
}
