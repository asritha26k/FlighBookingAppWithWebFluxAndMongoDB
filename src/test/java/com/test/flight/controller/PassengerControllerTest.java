package com.test.flight.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.flight.controller.PassengerController;
import com.flight.entity.Status;
import com.flight.entity.Ticket;
import com.flight.service.PassengerService;
import com.flight.service.TicketService;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class PassengerControllerTest {

	// call controller methods directly in unit tests

	@Mock
	private PassengerService passengerService;

	@Mock
	private TicketService ticketService;

	private PassengerController passengerController;

	@BeforeEach
	void setUp() {
		passengerController = new PassengerController();
		org.springframework.test.util.ReflectionTestUtils.setField(passengerController, "passengerService", passengerService);
		org.springframework.test.util.ReflectionTestUtils.setField(passengerController, "tickService", ticketService);

		// no WebTestClient; controller invoked directly
	}

	@Test
	void getTickets_Success() throws Exception {
		Ticket ticket = new Ticket();
		ticket.setPnr("PNR123");
		ticket.setSeatNo("12A");
		ticket.setStatus(Status.Booked);

		Mockito.when(passengerService.getTickets(Mockito.anyString()))
				.thenReturn(Mono.just(ResponseEntity.ok(List.of(ticket))));

		var resp = passengerController.getTickets("alice@example.com").block();
		org.junit.jupiter.api.Assertions.assertNotNull(resp);
		org.junit.jupiter.api.Assertions.assertNotNull(resp.getBody());
		org.junit.jupiter.api.Assertions.assertEquals(1, resp.getBody().size());
		org.junit.jupiter.api.Assertions.assertEquals("PNR123", resp.getBody().get(0).getPnr());
	}

	@Test
	void cancelBooking_Success() throws Exception {

		Mockito.when(ticketService.getDelete("PNR123")).thenReturn(Mono.just(ResponseEntity.status(HttpStatus.OK).build()));

		var resp = passengerController.getDeleted("PNR123").block();
		org.junit.jupiter.api.Assertions.assertNotNull(resp);
		org.junit.jupiter.api.Assertions.assertEquals(org.springframework.http.HttpStatus.OK, resp.getStatusCode());
	}
}
