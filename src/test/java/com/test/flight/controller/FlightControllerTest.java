package com.test.flight.controller;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.flight.controller.FlightController;
import com.flight.entity.Airline;
import com.flight.entity.Flight;
import com.flight.request.FlightReq;
import com.flight.request.SearchReq;
import com.flight.service.FlightService;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class FlightControllerTest {

    @Mock
    FlightService flightService;

    FlightController flightController;

    @BeforeEach
    void setUp() {
        flightController = new FlightController();
        org.springframework.test.util.ReflectionTestUtils.setField(flightController, "flightService", flightService);
    }

    public Flight createFlightEntity() {
        Flight flight = new Flight();
        flight.setFlightId("1");
        flight.setOrigin("India");
        flight.setDestination("Pakistan");
        flight.setPrice(200.0);
        flight.setDepartureTime(LocalDateTime.of(2025, 12, 1, 10, 30));
        flight.setArrivalTime(LocalDateTime.of(2025, 12, 1, 12, 0));
        flight.setAirline(Airline.Emirates);
        return flight;
    }

    public FlightReq createFlightReq() {
        FlightReq req = new FlightReq();
        req.setAirline(Airline.Emirates);
        req.setOrigin("India");
        req.setDestination("Pakistan");
        req.setPrice(200.0);
        req.setDepartureTime(LocalDateTime.of(2025, 12, 1, 10, 30));
        req.setArrivalTime(LocalDateTime.of(2025, 12, 1, 12, 0));
        return req;
    }

    @Test
    public void addControllerTest() throws Exception {
        FlightReq request = createFlightReq();
        Flight saved = createFlightEntity();

        when(flightService.addService(Mockito.any(com.flight.request.FlightReq.class)))
                .thenReturn(Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(saved.getFlightId())));

        var resp = flightController.addController(request).block();
        assertNotNull(resp);
        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertEquals("1", resp.getBody());
    }

    @Test
    public void searchControllerTest() throws Exception {
        Flight saved = createFlightEntity();
        List<Flight> flights = List.of(saved);

        when(flightService.searchService(Mockito.any(SearchReq.class)))
                .thenReturn(Mono.just(ResponseEntity.status(HttpStatus.OK).body(flights)));

        SearchReq req = new SearchReq();
        req.origin="India";
        req.destination="Pakistan";

        var resp = flightController.searchController(req).block();
        assertNotNull(resp);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(1, resp.getBody().size());
        assertEquals("1", resp.getBody().get(0).getFlightId());
    }

    @Test
    public void deleteFlightControllerTest() throws Exception {
        when(flightService.deleteFlightService("1"))
                .thenReturn(Mono.just(ResponseEntity.ok().build()));

        var resp = flightController.deleteFlightController("1").block();
        assertNotNull(resp);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }
}
