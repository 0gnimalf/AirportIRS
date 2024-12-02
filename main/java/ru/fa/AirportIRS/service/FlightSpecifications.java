package ru.fa.AirportIRS.service;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.fa.AirportIRS.models.Flight;
import ru.fa.AirportIRS.models.FlightStatusType;

import java.time.LocalDate;
import java.util.List;

public final class FlightSpecifications {
    private FlightSpecifications(){}

    public static Specification<Flight> hasKeyword(String keyword) {
        return (root, query, cb/*criteriaBuilder*/) -> {
            if(keyword == null || keyword.isEmpty()){
                return  null;
            }
            String likeKeyword = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("number")), likeKeyword),
                    cb.like(cb.lower(root.get("departureAirport").get("name")), likeKeyword),
                    cb.like(cb.lower(root.get("arrivalAirport").get("name")), likeKeyword),
                    cb.like(cb.lower(root.get("departureAirport").get("code")), likeKeyword),
                    cb.like(cb.lower(root.get("arrivalAirport").get("code")), likeKeyword),
                    cb.like(cb.lower(root.get("departureAirport").get("city")), likeKeyword),
                    cb.like(cb.lower(root.get("arrivalAirport").get("city")), likeKeyword),
                    cb.like(cb.lower(root.get("status")), likeKeyword),
                    cb.like(cb.lower(root.get("aircraft").get("model")), likeKeyword),
                    cb.like(cb.lower(root.get("aircraft").get("code")), likeKeyword),
                    cb.like(cb.lower(root.get("airline").get("code")), likeKeyword),
                    cb.like(cb.lower(root.get("airline").get("name")), likeKeyword)
            );
        };
    }
    public static Specification<Flight> inDateRange(LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction(); // Пустое условие
            if (startDate != null) {
                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(root.get("departureTime"), startDate));
            }
            if (endDate != null) {
                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(root.get("departureTime"), endDate));
            }
            if (startDate != null) {
                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(root.get("arrivalTime"), startDate));
            }
            if (endDate != null) {
                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(root.get("arrivalTime"), endDate));
            }
            return predicate;
        };
    }
    public static Specification<Flight> hasStatus(List<FlightStatusType> statuses) {
        return (root, query, cb) -> {
            if (statuses == null) {
                return null;
            }
            return root.get("status").in(statuses);
        };
    }
    public static Specification<Flight> hasYear(Integer year) {
        return (root, query, cb) -> {
            if (year == null) {
                return null;
            }
            return cb.equal(cb.function("YEAR", Integer.class, root.get("departureTime")), year);
        };
    }
    public static Specification<Flight> arrival(boolean arrival, String myAirport) {
        return (root, query, cb) -> {
            if (arrival) {
                return cb.equal(root.get("arrivalAirport").get("code"), myAirport);
            }else {
                return cb.equal(root.get("departureAirport").get("code"), myAirport);
            }
        };
    }
}
