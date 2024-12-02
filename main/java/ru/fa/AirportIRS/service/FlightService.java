package ru.fa.AirportIRS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.fa.AirportIRS.models.Flight;
import ru.fa.AirportIRS.models.FlightStatusType;
import ru.fa.AirportIRS.repository.FlightRepository;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FlightService {
    @Autowired
    private FlightRepository repo;
    public List<Flight> getAllFlights() {return repo.findAll();}
    public Flight getById(long id) {return repo.findById(id).orElseThrow();}
    public void addFlight(Flight flight) {repo.save(flight);}
    public void update(Flight flight) {repo.save(flight);}
    public void deleteById(long id) {repo.deleteById(id);}

    public Page<Flight> getPageSpec(String keyword, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Specification<Flight> specification = Specification
                .where(FlightSpecifications.hasKeyword(keyword))
                .and(FlightSpecifications.inDateRange(startDate, endDate));

        return repo.findAll(specification, pageable);}

    public Page<Flight> getPageSpecUser(boolean arrival, String myAirport, String keyword, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Specification<Flight> specification = Specification
                .where(FlightSpecifications.hasKeyword(keyword))
                .and(FlightSpecifications.inDateRange(startDate, endDate))
                .and(FlightSpecifications.arrival(arrival, myAirport));
        return repo.findAll(specification, pageable);}

    public Map<String, Map<String, Integer>> getByStatusAndYear(List<FlightStatusType> statuses, Integer year) {
        Specification<Flight> specification = Specification
                .where(FlightSpecifications.hasStatus(statuses))
                .and(FlightSpecifications.hasYear(year));


        List<Flight> flights = repo.findAll(specification);
        return flights.stream()
                .collect(Collectors.groupingBy(
                        flight -> flight.getStatus().getName(), // Группировка по статусу
                        Collectors.collectingAndThen(
                                Collectors.groupingBy(
                                        flight -> flight.getDepartureTime().toLocalDate().toString(), // Группировка по дням
                                        Collectors.summingInt(f -> 1) // Подсчёт количества рейсов
                                ),
                                map -> map.entrySet().stream()
                                        .sorted(Map.Entry.comparingByKey()) // Сортировка по ключам (год-месяц)
                                        .collect(Collectors.toMap(
                                                Map.Entry::getKey,
                                                Map.Entry::getValue,
                                                (e1, e2) -> e1,
                                                LinkedHashMap::new // Сохранение порядка сортировки
                                        ))
                        )
                ));

    }
}
