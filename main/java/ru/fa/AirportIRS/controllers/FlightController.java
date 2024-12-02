package ru.fa.AirportIRS.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.fa.AirportIRS.models.DTO.FlightDetails;
import ru.fa.AirportIRS.models.Flight;
import ru.fa.AirportIRS.models.FlightStatusType;
import ru.fa.AirportIRS.repository.AircraftRepository;
import ru.fa.AirportIRS.repository.AirlineRepository;
import ru.fa.AirportIRS.service.AirportService;
import ru.fa.AirportIRS.service.FlightService;

import java.time.LocalDate;

@Controller
public class FlightController {
    @Autowired
    private FlightService flightService;
    @Autowired
    private AirportService airportService;
    @Autowired
    private AircraftRepository aircraftRepository;
    @Autowired
    private AirlineRepository airlineRepository;

    private final Sort.Direction directionDesc = Sort.Direction.DESC;
    private final Sort.Direction directionAsc = Sort.Direction.ASC;

    @Value("${myAirport}")
    private String myAirport;

    private void returnPageToModel(Model model, int page, String keyword,
                                   LocalDate startDate, LocalDate endDate,
                                   Page<Flight> flightsPage) {
        if (!flightsPage.isEmpty()) {
            model.addAttribute("flightsPage", flightsPage);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", flightsPage.getTotalPages());
        } else {
            model.addAttribute("flightsPage", Page.empty());
        }
        model.addAttribute("keyword", keyword);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
    }
    private void returnValuesToModel(Model model){
        model.addAttribute("airports", airportService.getAll());
        model.addAttribute("aircrafts", aircraftRepository.findAll(Sort.by(directionAsc, "model")));
        model.addAttribute("statuses", FlightStatusType.values());
        model.addAttribute("airlines", airlineRepository.findAll(Sort.by(directionAsc, "name")));
    }

    @GetMapping("/admin/flights")
    public String getFlights(Model model,
                             @RequestParam(name = "page", defaultValue = "0") int page,
                             @RequestParam(name = "keyword", required = false) String keyword,
                             @RequestParam(name = "startDate", required = false) LocalDate startDate,
                             @RequestParam(name = "endDate", required = false) LocalDate endDate
    ) {
        int pageSize = 10;
        Page<Flight> flightsPage = flightService.getPageSpec(keyword, startDate, endDate, (PageRequest.of(page, pageSize, Sort.by(directionDesc, "departureTime"))));

        returnPageToModel(model, page, keyword, startDate, endDate, flightsPage);
        return "admin/flights";
    }

    @GetMapping("/admin/flights/new")
    public String getNewFlight(Model model) {
        model.addAttribute("newFlight", new Flight());
        returnValuesToModel(model);
        return "admin/new-flight";
    }

    @PostMapping("/admin/flights")
    public String postNewFlight(@ModelAttribute("newFlight") Flight flight) {
        flightService.addFlight(flight);
        return "redirect:/admin/flights";
    }

    @GetMapping("/admin/flights/{id}/edit")
    public String editFlight(@PathVariable Long id, Model model) {
        Flight flight = flightService.getById(id);
        model.addAttribute("flight", flight);
        returnValuesToModel(model);
        return "admin/edit-flight";
    }

    @PostMapping("/admin/flights/{id}")
    public String updateFlight(@ModelAttribute Flight flight) {
        flightService.update(flight);
        return "redirect:/admin/flights";
    }

    @GetMapping("/admin/flights/{id}/delete")
    public String deleteFlight(@PathVariable Long id) {
        flightService.deleteById(id);
        return "redirect:/admin/flights";
    }
    // для обычного юзера
    @GetMapping("/home/arrival")
    public String getArrival(Model model,
                             @RequestParam(name = "page", defaultValue = "0") int page,
                             @RequestParam(name = "keyword", required = false) String keyword,
                             @RequestParam(name = "startDate", required = false) LocalDate startDate,
                             @RequestParam(name = "endDate", required = false) LocalDate endDate
    ) {
        int pageSize = 15;
        Page<Flight> flightsPage = flightService.getPageSpecUser(true, myAirport, keyword, startDate, endDate, (PageRequest.of(page, pageSize, Sort.by(directionDesc, "arrivalTime"))));

        returnPageToModel(model, page, keyword, startDate, endDate, flightsPage);
        return "home/arrival-board";
    }

    @GetMapping("/home/departure")
    public String getDeparture(Model model,
                               @RequestParam(name = "page", defaultValue = "0") int page,
                               @RequestParam(name = "keyword", required = false) String keyword,
                               @RequestParam(name = "startDate", required = false) LocalDate startDate,
                               @RequestParam(name = "endDate", required = false) LocalDate endDate
    ) {
        int pageSize = 15;
        Page<Flight> flightsPage = flightService.getPageSpecUser(false, myAirport, keyword, startDate, endDate, (PageRequest.of(page, pageSize, Sort.by(directionDesc, "departureTime"))));

        returnPageToModel(model, page, keyword, startDate, endDate, flightsPage);
        return "home/departure-board";
    }


    @GetMapping("/api/flight/{id}")
    public ResponseEntity<?> getFlightDetails(@PathVariable Long id) {
        try {
            Flight flight = flightService.getById(id);
            FlightDetails flightDetails = new FlightDetails(
                    flight.getNumber(),
                    flight.getDepartureAirport().getCity(),
                    flight.getArrivalAirport().getCity(),
                    flight.getDepartureTime(),
                    flight.getArrivalTime(),
                    flight.getAircraft().getModel(),
                    flight.getStatus().getName(),
                    flight.getAirline().getName() != null ? flight.getAirline().getName() : "-пусто-",
                    flight.getAirline().getPhone() != null ? flight.getAirline().getPhone() : "-пусто-",
                    flight.getAirline().getEmail() != null ? flight.getAirline().getEmail() : "-пусто-",
                    flight.getAirline().getWebsite() != null ? flight.getAirline().getWebsite() : "-пусто-"
            );
            return ResponseEntity.ok(flightDetails);
        } catch (Exception e){return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Flight not found");}
    }
}
