package com.flight.repository;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.flight.entity.Flight;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FlightRepository extends ReactiveMongoRepository<Flight, String> {
	Flux<Flight> findByOrigin(String origin);

	Flux<Flight> findByDestination(String destination);

	Flux<Flight> findByOriginAndDestination(String origin, String destination);

	Mono<Flight> findByflightId(String flightId);

}
