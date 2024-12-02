package ru.fa.AirportIRS.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class Airline {
    @Id
    private String code;
    private String name;
    private String website;
    private String phone;
    private String email;
}
