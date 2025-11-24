package com.test.flight.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.flight.entity.Flight;
import com.flight.entity.Passenger;
import com.flight.entity.Status;
import com.flight.entity.Ticket;
import com.flight.exception.ResourceNotFoundException;
import com.flight.exception.ResourceNotFoundExceptionForResponseEntity;
import com.flight.repository.FlightRepository;
import com.flight.repository.PassengerRepository;
import com.flight.repository.TicketRepository;
import com.flight.request.TicketBookingRequest;
import com.flight.service.TicketService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

	@Mock
	private TicketRepository ticketRepo;

	@Mock
	private PassengerRepository passRepo;

	@Mock
	private FlightRepository flightRepo;

	@InjectMocks
	private TicketService ticketService;

	private Passenger passenger;
	private Flight flight;
	private TicketBookingRequest request;

	@BeforeEach
	void setUp() {
		passenger = new Passenger();
		passenger.setPassengerId("1");
		//passenger.setTicket(new ArrayList<>());

		flight = new Flight();
		flight.setFlightId("100");

		request = new TicketBookingRequest();
		request.passenger_id = "1";
		request.seatNo = "A1";
	}

	@Test
	void testBookTicketService_Success() throws ResourceNotFoundException {
		when(passRepo.findById("1")).thenReturn(Mono.just(passenger));
		when(flightRepo.findById("100")).thenReturn(Mono.just(flight));
		// ticketRepo.save returns Mono<Ticket> in service, so return a Mono here
		when(ticketRepo.save(any(Ticket.class))).thenAnswer(i -> Mono.just((Ticket) i.getArguments()[0]));

		Mono<ResponseEntity<String>> pnr = ticketService.bookTicketService("100", request);

		assertNotNull(pnr.block().getBody());
		verify(ticketRepo, times(1)).save(any(Ticket.class));
	}

	@Test
	void testBookTicketService_PassengerNotFound() {
		when(passRepo.findById("1")).thenReturn(Mono.empty());

		ResourceNotFoundExceptionForResponseEntity exception = assertThrows(
				ResourceNotFoundExceptionForResponseEntity.class,
				() -> ticketService.bookTicketService("100", request).block());

		assertEquals("Passenger with id 1 not found", exception.getMessage());
	}

	@Test
	void testBookTicketService_FlightNotFound() {
		when(passRepo.findById("1")).thenReturn(Mono.just(passenger));
		when(flightRepo.findById("100")).thenReturn(Mono.empty());

		ResourceNotFoundExceptionForResponseEntity exception = assertThrows(
				ResourceNotFoundExceptionForResponseEntity.class,
				() -> ticketService.bookTicketService("100", request).block());

		assertEquals("Flight with id 100 not found", exception.getMessage());
	}

	@Test
	void testGetServiceDetails() throws ResourceNotFoundException {
		Ticket ticket = new Ticket();
		ticket.setPnr("PNR12345");

		when(ticketRepo.findByPnr("PNR12345")).thenReturn(Mono.just(ticket));

		Mono<ResponseEntity<Ticket>> result = ticketService.getServiceDetails("PNR12345");

		assertNotNull(result);
		assertEquals("PNR12345", result.block().getBody().getPnr());
	}

	@Test
	void testGetServiceDetails_NotFound() throws ResourceNotFoundExceptionForResponseEntity {
		when(ticketRepo.findByPnr("MISSING")).thenReturn(Mono.empty());

		ResourceNotFoundExceptionForResponseEntity exception = assertThrows(
				ResourceNotFoundExceptionForResponseEntity.class,
				() -> ticketService.getServiceDetails("MISSING").block());
		assertEquals("MISSING this pnr details not found", exception.getMessage());
	}

	@Test
	void testGetDelete_Success() throws ResourceNotFoundException {
		Passenger localPassenger = new Passenger();
		localPassenger.setPassengerId("1");

		Ticket ticket = new Ticket();
		ticket.setPnr("PNR12345");
		ticket.setPassengerId(localPassenger.getPassengerId());
		ticket.setStatus(Status.Booked);
		ticket.setSeatNo("A1");


		when(ticketRepo.findByPnr("PNR12345")).thenReturn(Mono.just(ticket));
		when(ticketRepo.delete(ticket)).thenReturn(Mono.empty());

		Mono<ResponseEntity<Void>> result = ticketService.getDelete("PNR12345");

		assertEquals(HttpStatus.OK, result.block().getStatusCode());
		verify(ticketRepo, times(1)).delete(ticket);

	}

	@Test
	void testGetDelete_TicketNotFound() {
		String p = "PNR12345";
		when(ticketRepo.findByPnr(p)).thenReturn(Mono.empty());

		ResourceNotFoundExceptionForResponseEntity exception = assertThrows(
				ResourceNotFoundExceptionForResponseEntity.class,
				() -> ticketService.getDelete("PNR12345").block());

		assertEquals(p + " this pnr details not found", exception.getMessage());
	}
}
