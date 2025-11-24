package com.test.flight.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.flight.entity.Airline;
import com.flight.entity.Flight;
import com.flight.exception.ResourceNotFoundExceptionForResponseEntity;
import com.flight.repository.FlightRepository;
import com.flight.request.FlightReq;
import com.flight.request.SearchReq;
import com.flight.service.FlightService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    FlightRepository flightRepo;

    @InjectMocks
    FlightService flightService;

    private Flight createFlightEntity() {
        Flight input = new Flight();
        input.setAirline(Airline.Emirates);
        input.setFlightId("1");
        input.setOrigin("india");
        input.setDestination("pakistan");
        return input;
    }

    private FlightReq createFlightReq() {
        FlightReq req = new FlightReq();
        req.setAirline(Airline.Emirates);
        req.setOrigin("india");
        req.setDestination("pakistan");
        req.setPrice(200.0);
        return req;
    }

    @Test
    public void testAddService() {
        FlightReq req = createFlightReq();
        Flight saved = createFlightEntity();

        when(flightRepo.save(Mockito.any(Flight.class))).thenReturn(Mono.just(saved));

        Mono<ResponseEntity<String>> output = flightService.addService(req);
        assertEquals(HttpStatus.CREATED, output.block().getStatusCode());
        verify(flightRepo, times(1)).save(Mockito.any(Flight.class));
    }

    @Test
    public void testSearchService() {
        SearchReq searchReq = new SearchReq();
        searchReq.origin="india";
        searchReq.destination="pakistan";
        List<Flight> flights = new ArrayList<>();

        Flight input = createFlightEntity();
        flights.add(input);

        when(flightRepo.findByOriginAndDestination("india", "pakistan")).thenReturn(Flux.fromIterable(flights));

        Mono<ResponseEntity<List<Flight>>> output = flightService.searchService(searchReq);
        assertEquals(flights, output.block().getBody());
        verify(flightRepo, times(1)).findByOriginAndDestination("india", "pakistan");
    }

    @Test
    public void testFailedSearchService() {
        SearchReq searchReq = new SearchReq();
        searchReq.origin="india";
        searchReq.destination="pakistan";
        List<Flight> flights = new ArrayList<>();

        when(flightRepo.findByOriginAndDestination("india", "pakistan")).thenReturn(Flux.fromIterable(flights));

        ResourceNotFoundExceptionForResponseEntity exception = assertThrows(
                ResourceNotFoundExceptionForResponseEntity.class,
                () -> flightService.searchService(searchReq).block());

        assertEquals("No flights found from india to pakistan", exception.getMessage());
    }

    @Test
    public void Deletion() {
        Flight input = createFlightEntity();
        when(flightRepo.findById("1")).thenReturn(Mono.just(input));
        when(flightRepo.delete(input)).thenReturn(Mono.empty());
        Mono<ResponseEntity<Void>> response = flightService.deleteFlightService("1");
        ResponseEntity<Void> resp = response.block();
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNull(resp.getBody());
        verify(flightRepo, times(1)).delete(input);
    }

    @Test
    public void DeletionFailed() {
        when(flightRepo.findById("1")).thenReturn(Mono.empty());
        assertThrows(ResourceNotFoundExceptionForResponseEntity.class,
                () -> flightService.deleteFlightService("1").block());
    }
}
