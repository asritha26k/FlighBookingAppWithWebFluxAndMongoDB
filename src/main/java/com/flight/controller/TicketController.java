package com.flight.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.flight.entity.Ticket;
import com.flight.exception.ResourceNotFoundExceptionForResponseEntity;
import com.flight.request.TicketBookingRequest;
import com.flight.service.TicketService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
public class TicketController {

	@Autowired
	TicketService ticketService;

	@PostMapping("/api/flight/booking/{f_id}")
	public Mono<ResponseEntity<String>> bookTicket(@PathVariable String f_id, @Valid @RequestBody TicketBookingRequest req)
			throws ResourceNotFoundExceptionForResponseEntity {
		return ticketService.bookTicketService(f_id, req);

	}

	@GetMapping("/api/flight/ticket/{pnr}")
	public Mono<ResponseEntity<Ticket>> getDetails(@PathVariable String pnr)
			throws ResourceNotFoundExceptionForResponseEntity {
		return ticketService.getServiceDetails(pnr);
	}

}
