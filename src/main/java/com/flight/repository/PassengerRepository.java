package com.flight.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.flight.entity.Passenger;

import reactor.core.publisher.Mono;

public interface PassengerRepository extends ReactiveMongoRepository<Passenger, String> {

	Mono<Passenger> findByEmailId(String emailId);
	Mono<Passenger> findById(String id);
    Mono<Boolean> existsByEmailId(String emailId);


}
