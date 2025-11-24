package com.flight.request;

import java.time.LocalDateTime;
import com.flight.entity.Airline;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FlightReq {

    @NotNull
    private Airline airline;

    @NotBlank
    private String origin;

    @NotBlank
    private String destination;

    @Min(0)
    private Double price;

    @NotNull
    private LocalDateTime departureTime;

    @NotNull
    private LocalDateTime arrivalTime;

    public Airline getAirline() {
        return airline;
    }

    public void setAirline(Airline emirates) {
        this.airline = emirates;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
