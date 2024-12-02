package ru.fa.AirportIRS.models;

import lombok.Getter;

@Getter
public enum FlightStatusType {
    Scheduled("Планируется"),
    InAir("В воздухе"),
    Delayed("Задержан"),
    Canceled("Отменен"),
    Arrived("Прибыл вовремя");
    private final String name;
    FlightStatusType(String name) {this.name = name;}
}
