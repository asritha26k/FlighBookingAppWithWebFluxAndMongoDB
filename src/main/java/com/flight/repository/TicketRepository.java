package com.flight.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.flight.entity.Ticket;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TicketRepository extends ReactiveMongoRepository<Ticket, String> {

	Mono<Ticket> findByPnr(String pnr);

	Flux<Ticket> findAllByPassengerId(String string);
    

}
