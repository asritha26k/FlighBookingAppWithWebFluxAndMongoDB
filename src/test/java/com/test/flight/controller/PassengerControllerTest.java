package com.test.flight.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.flight.GlobalExceptionHandler;
import com.flight.controller.PassengerController;
import com.flight.entity.Status;
import com.flight.entity.Ticket;
import com.flight.service.PassengerService;
import com.flight.service.TicketService;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class PassengerControllerTest {

	private MockMvc mockMvc;

	@Mock
	private PassengerService passengerService;

	@Mock
	private TicketService ticketService;

	@InjectMocks
	private PassengerController passengerController;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(passengerController)
				.setControllerAdvice(new GlobalExceptionHandler())
				.build();
	}

	@Test
	void getTickets_Success() throws Exception {
		Ticket ticket = new Ticket();
		ticket.setPnr("PNR123");
		ticket.setSeatNo("12A");
		ticket.setStatus(Status.Booked);

		when(passengerService.getTickets("alice@example.com"))
			.thenReturn(Mono.just(ResponseEntity.ok(List.of(ticket))));

		mockMvc.perform(get("/api/flight/booking/history/{email}", "alice@example.com").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].pnr").value("PNR123"));
	}

	@Test
	void cancelBooking_Success() throws Exception {

		when(ticketService.getDelete("PNR123")).thenReturn(Mono.just(ResponseEntity.status(HttpStatus.OK).build()));

		mockMvc.perform(delete("/api/flight/booking/cancel/{pnr}", "PNR123")).andExpect(status().isOk()).andExpect(result -> {
			
			assert(result.getResponse().getContentAsString().isEmpty());
		});
	}
}
