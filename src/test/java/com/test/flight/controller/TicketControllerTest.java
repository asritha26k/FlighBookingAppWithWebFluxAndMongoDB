package com.test.flight.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flight.controller.TicketController;
import com.flight.entity.Status;
import com.flight.entity.Ticket;
import com.flight.request.TicketBookingRequest;
import com.flight.service.TicketService;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

	private MockMvc mockMvc;
	private ObjectMapper mapper = new ObjectMapper();

	@Mock
	private TicketService ticketService;

	@InjectMocks
	private TicketController ticketController;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();
	}

	@Test
	void bookTicket_Success() throws Exception {
		TicketBookingRequest req = new TicketBookingRequest();
		req.passenger_id = "1";
		req.seatNo = "A1";

		Mockito.when(ticketService.bookTicketService(anyString(), any(TicketBookingRequest.class)))
				.thenReturn(Mono.just(ResponseEntity.status(HttpStatus.CREATED).body("PNR12345")));

		mockMvc.perform(post("/api/flight/booking/100").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(req))).andExpect(status().isCreated())
				.andExpect(content().string("PNR12345"));
	}

	@Test
	void getTicket_Success() throws Exception {
		Ticket ticket = new Ticket();
		ticket.setTicketId("7");
		ticket.setPnr("PNR12345");
		ticket.setSeatNo("12A");
		ticket.setStatus(Status.Booked);

		Mockito.when(ticketService.getServiceDetails("PNR12345")).thenReturn(Mono.just(ResponseEntity.ok(ticket)));

		mockMvc.perform(get("/api/flight/ticket/PNR12345").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.pnr").value("PNR12345"))
				.andExpect(jsonPath("$.seatNo").value("12A")).andExpect(jsonPath("$.status").value("Booked"));
	}
}
