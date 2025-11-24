package com.flight.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.flight.entity.Flight;
import com.flight.exception.ResourceNotFoundExceptionForResponseEntity;
import com.flight.request.FlightReq;
import com.flight.request.SearchReq;
import com.flight.service.FlightService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
public class FlightController {

	@Autowired
	FlightService flightService;

	// flight added
	@PostMapping("/api/flight/airline/inventory/add")
	public Mono<ResponseEntity<String>> addController(@Valid @RequestBody FlightReq flight) {
		return flightService.addService(flight);
	}

	@PostMapping("/api/flight/search")
	public Mono<ResponseEntity<List<Flight>>> searchController(@Valid @RequestBody SearchReq searchReq)
			throws ResourceNotFoundExceptionForResponseEntity {

		return flightService.searchService(searchReq);
	}

	@DeleteMapping("/api/flight/airline/inventory/delete/{flightId}")
	public Mono<ResponseEntity<Void>> deleteFlightController(@PathVariable String flightId)
			throws ResourceNotFoundExceptionForResponseEntity {
		return flightService.deleteFlightService(flightId);
	}

}
