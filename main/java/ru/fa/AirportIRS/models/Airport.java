package ru.fa.AirportIRS.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class Airport {
    @Id
    private String code;
    private String name;
    private String city;
    private Double latitude;
    private Double longitude;
}
