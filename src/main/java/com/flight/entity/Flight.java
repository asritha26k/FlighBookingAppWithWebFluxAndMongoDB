package com.flight.entity;

import java.time.LocalDateTime;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Document("flights")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flight {
    @Id
    private String flightId;
    private Airline airline;
    private String origin;
    private String destination;
    private Double price;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
}
