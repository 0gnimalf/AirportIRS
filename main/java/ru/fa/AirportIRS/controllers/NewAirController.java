package ru.fa.AirportIRS.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.fa.AirportIRS.models.Aircraft;
import ru.fa.AirportIRS.models.Airline;
import ru.fa.AirportIRS.models.Airport;
import ru.fa.AirportIRS.repository.AircraftRepository;
import ru.fa.AirportIRS.repository.AirlineRepository;
import ru.fa.AirportIRS.repository.AirportRepository;

@Controller
@RequestMapping("/admin/new")
public class NewAirController {
    @Autowired
    AirlineRepository airlineRepository;
    @Autowired
    AircraftRepository aircraftRepository;
    @Autowired
    AirportRepository airportRepository;

    @GetMapping("/airline")
    public String getAirline(Model model) {
        model.addAttribute("nAirline", new Airline());
        return "/admin/new/new-airline";
    }
    @PostMapping("/airline")
    public String addAirline(@ModelAttribute("nAirline") Airline airline) {
        airlineRepository.save(airline);
        return "/admin/new/new-airline";
    }
    @GetMapping("/aircraft")
    public String getAircraft(Model model) {
        model.addAttribute("nAircraft", new Aircraft());
        return "/admin/new/new-aircraft";
    }
    @PostMapping("/aircraft")
    public String addAircraft(@ModelAttribute("nAircraft") Aircraft aircraft) {
        aircraftRepository.save(aircraft);
        return "/admin/new/new-aircraft";
    }
    @GetMapping("/airport")
    public String getAirport(Model model) {
        model.addAttribute("nAirport", new Airport());
        return "/admin/new/new-airport";
    }
    @PostMapping("/airport")
    public String addAirport(@ModelAttribute("nAirport") Airport airport) {
        airportRepository.save(airport);
        return "/admin/new/new-airport";
    }
}
