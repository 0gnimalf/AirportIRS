package ru.fa.AirportIRS.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class Aircraft {
    @Id
    private String code;
    private String model;
    private Integer range;
}
