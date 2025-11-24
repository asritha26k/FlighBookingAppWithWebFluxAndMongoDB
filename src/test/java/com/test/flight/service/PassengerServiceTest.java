package com.test.flight.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.flight.entity.Address;
import com.flight.entity.Passenger;
import com.flight.entity.Ticket;
import com.flight.exception.ResourceNotFoundExceptionForResponseEntity;
import com.flight.repository.AddressRepository;
import com.flight.repository.PassengerRepository;
import com.flight.repository.TicketRepository;
import com.flight.request.PassengerRequest;
import com.flight.service.PassengerService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PassengerServiceTest {

	@Mock
	private PassengerRepository passRepo;

	@Mock
	private AddressRepository addRepo;

	@Mock
	private TicketRepository tickRepo;

	@InjectMocks
	private PassengerService passengerService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testAddPassenger() {
		PassengerRequest req = new PassengerRequest();
		req.name = "Asritha";
		req.phoneNum = (long) 9876543;
		req.emailId = "asritha@example.com";
		req.city = "Bangalore";
		req.houseNo = 123;
		req.state = "KA";
		req.country = "India";

		Passenger passenger = new Passenger();
		passenger.setName(req.name);
		passenger.setPhoneNo(req.phoneNum);
		passenger.setEmailId(req.emailId);
		// ensure a passengerId is present so address can reference it
		passenger.setPassengerId("1");

		Address address = new Address();
		address.setCity(req.city);
		address.setHouseNo(req.houseNo);
		address.setState(req.state);
		address.setCountry(req.country);
		address.setPassengerId(passenger.getPassengerId());

		when(passRepo.save(any(Passenger.class))).thenReturn(Mono.just(passenger));
		// mock address save as the service saves address after passenger
		when(addRepo.save(any(Address.class))).thenReturn(Mono.just(address));

		Mono<ResponseEntity<String>> savedPassenger = passengerService.add(req);

		assertEquals(savedPassenger.block().getStatusCode(), HttpStatus.CREATED);
		verify(passRepo, times(1)).save(any(Passenger.class));
	}

	@Test
	void testGetTicketsFound() throws ResourceNotFoundExceptionForResponseEntity {
		String email = "asritha@example.com";

		Passenger passenger = new Passenger();
		passenger.setPassengerId("1");
		passenger.setEmailId(email);

		Ticket ticket1 = new Ticket();
		Ticket ticket2 = new Ticket();
		List<Ticket> tickets = new ArrayList<>();
		tickets.add(ticket1);
		tickets.add(ticket2);

		when(passRepo.findByEmailId(email)).thenReturn(Mono.just(passenger));
		when(tickRepo.findAllByPassengerId("1")).thenReturn(Flux.fromIterable(tickets));

		Mono<ResponseEntity<List<Ticket>>> result = passengerService.getTickets(email);

		assertEquals(2, result.block().getBody().size());
		verify(passRepo, times(1)).findByEmailId(email);
		verify(tickRepo, times(1)).findAllByPassengerId("1");
	}

	@Test
	void testGetTicketsNotFound() {
		String email = "unknown@example.com";

		when(passRepo.findByEmailId(email)).thenReturn(Mono.empty());

		ResourceNotFoundExceptionForResponseEntity exception = assertThrows(
				ResourceNotFoundExceptionForResponseEntity.class, () -> {
					passengerService.getTickets(email).block();
				});

		assertEquals("Passenger with " + email + " not found", exception.getMessage());
		verify(passRepo, times(1)).findByEmailId(email);
		verify(tickRepo, never()).findAllByPassengerId(anyString());
	}
}
