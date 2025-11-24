package com.flight.service;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.flight.entity.Flight;
import com.flight.entity.Passenger;
import com.flight.entity.Status;
import com.flight.entity.Ticket;
import com.flight.exception.ResourceNotFoundExceptionForResponseEntity;
import com.flight.repository.FlightRepository;
import com.flight.repository.PassengerRepository;
import com.flight.repository.TicketRepository;
import com.flight.request.TicketBookingRequest;

import reactor.core.publisher.Mono;

@Service
public class TicketService {
	@Autowired
	TicketRepository ticketRepo;

	@Autowired
	PassengerRepository passRepo;

	@Autowired
	FlightRepository flightRepo;

	public Mono<ResponseEntity<String>> bookTicketService(String i, TicketBookingRequest req) 
	        throws ResourceNotFoundExceptionForResponseEntity {
	    return passRepo.findById(req.passenger_id)
	            .switchIfEmpty(Mono.error(new ResourceNotFoundExceptionForResponseEntity(
	                    "Passenger with id " + req.passenger_id + " not found")))
	            .flatMap(passenger ->
	                flightRepo.findById(i)
	                          .switchIfEmpty(Mono.error(new ResourceNotFoundExceptionForResponseEntity(
	                                  "Flight with id " + i + " not found")))
	                          .flatMap(flight -> {
	                              Ticket newTicket = new Ticket();
	                              newTicket.setFlightId(flight.getFlightId()); // FK only
	                              newTicket.setPassengerId(passenger.getPassengerId()); // FK only
	                              newTicket.setSeatNo(req.seatNo);
	                              newTicket.setStatus(Status.Booked);

	                              String pnr = UUID.randomUUID().toString().substring(0, 8);
	                              newTicket.setPnr(pnr);

	                              return ticketRepo.save(newTicket)
	                                               .map(saved -> ResponseEntity.status(HttpStatus.CREATED)
	                                                                           .body(pnr));
	                          })
	            );
	}



	public Mono<ResponseEntity<Ticket>> getServiceDetails(String pnr) throws ResourceNotFoundExceptionForResponseEntity {

		
		return ticketRepo.findByPnr(pnr).switchIfEmpty(
				
				Mono.error(new ResourceNotFoundExceptionForResponseEntity(pnr + " this pnr details not found"))
).map(ticket-> ResponseEntity.ok(ticket));
	}

	public Mono<ResponseEntity<Void>> getDelete(String pnr) throws ResourceNotFoundExceptionForResponseEntity {
	    return ticketRepo.findByPnr(pnr)
	            .switchIfEmpty(Mono.error(new ResourceNotFoundExceptionForResponseEntity(
	                    pnr + " this pnr details not found")))
	            .flatMap(ticket ->
	                ticketRepo.delete(ticket)  // delete returns Mono<Void>
	                          .then(Mono.just(ResponseEntity.ok().build()))
	            );
//	    we use then when we only want to run something AFTER it completes
	    //we cant just remove then because Because map needs a value — delete returns no value.
	    
	}

}








