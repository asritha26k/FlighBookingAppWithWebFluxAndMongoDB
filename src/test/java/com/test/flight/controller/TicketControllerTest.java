package com.test.flight.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.flight.controller.TicketController;
import com.flight.entity.Status;
import com.flight.entity.Ticket;
import com.flight.request.TicketBookingRequest;
import com.flight.service.TicketService;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

	// no ObjectMapper needed when calling controller methods directly

	@Mock
	private TicketService ticketService;

	private TicketController ticketController;

	@BeforeEach
	void setUp() {
		ticketController = new TicketController();
		org.springframework.test.util.ReflectionTestUtils.setField(ticketController, "ticketService", ticketService);
	}

	@Test
	void bookTicket_Success() throws Exception {
		TicketBookingRequest req = new TicketBookingRequest();
		req.passenger_id = "1";
		req.seatNo = "A1";

		Mockito.when(ticketService.bookTicketService(anyString(), any(TicketBookingRequest.class)))
				.thenReturn(Mono.just(ResponseEntity.status(HttpStatus.CREATED).body("PNR12345")));

		var resp = ticketController.bookTicket("100", req).block();
		org.junit.jupiter.api.Assertions.assertNotNull(resp);
		org.junit.jupiter.api.Assertions.assertNotNull(resp.getBody());
		org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.CREATED, resp.getStatusCode());
		org.junit.jupiter.api.Assertions.assertEquals("PNR12345", resp.getBody());
	}

	@Test
	void getTicket_Success() throws Exception {
		Ticket ticket = new Ticket();
		ticket.setTicketId("7");
		ticket.setPnr("PNR12345");
		ticket.setSeatNo("12A");
		ticket.setStatus(Status.Booked);

		Mockito.when(ticketService.getServiceDetails("PNR12345")).thenReturn(Mono.just(ResponseEntity.ok(ticket)));

		var resp = ticketController.getDetails("PNR12345").block();
		org.junit.jupiter.api.Assertions.assertNotNull(resp);
		org.junit.jupiter.api.Assertions.assertNotNull(resp.getBody());
		org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.OK, resp.getStatusCode());
		org.junit.jupiter.api.Assertions.assertEquals("PNR12345", resp.getBody().getPnr());
		org.junit.jupiter.api.Assertions.assertEquals("12A", resp.getBody().getSeatNo());
		org.junit.jupiter.api.Assertions.assertEquals(Status.Booked, resp.getBody().getStatus());
	}
}