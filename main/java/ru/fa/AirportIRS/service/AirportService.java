package ru.fa.AirportIRS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.fa.AirportIRS.models.Airport;
import ru.fa.AirportIRS.repository.AirportRepository;

import java.util.List;

@Service
public class AirportService {
    @Autowired
    private AirportRepository repo;
    public List<Airport> getAll() {return repo.findAll(Sort.by(Sort.Direction.ASC, "name"));}
    public Airport getById(String code) {return repo.findById(code).get();}
}
