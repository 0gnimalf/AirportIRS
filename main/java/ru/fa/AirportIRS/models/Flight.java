package ru.fa.AirportIRS.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Entity
@Table
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;
    @ManyToOne
    @JoinColumn(name = "departure_airport")
    private Airport departureAirport;
    @ManyToOne
    @JoinColumn(name = "arrival_airport")
    private Airport arrivalAirport;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime departureTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime arrivalTime;
    @ManyToOne
    @JoinColumn(name = "aircraft")
    private Aircraft aircraft;
    @Enumerated(EnumType.STRING)
    private FlightStatusType status;
    @ManyToOne
    @JoinColumn(name = "airline")
    private Airline airline;
}
