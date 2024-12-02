package ru.fa.AirportIRS.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class FlightDetails {
    private String number;
    private String departureCity;
    private String arrivalCity;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String aircraft;
    private String status;
    private String airlineName;
    private String airlinePhone;
    private String airlineEmail;
    private String airlineWebsite;
}
