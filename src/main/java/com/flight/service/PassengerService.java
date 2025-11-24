package com.flight.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.flight.entity.Address;
import com.flight.entity.Passenger;
import com.flight.entity.Ticket;
import com.flight.exception.ResourceNotFoundExceptionForResponseEntity;
import com.flight.repository.AddressRepository;
import com.flight.repository.PassengerRepository;
import com.flight.repository.TicketRepository;
import com.flight.request.PassengerRequest;
import com.mongodb.DuplicateKeyException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PassengerService {
	@Autowired
	PassengerRepository passRepo;

	@Autowired
	AddressRepository addRepo;

	@Autowired
	TicketRepository tickRepo;
	public Mono<ResponseEntity<String>> add(PassengerRequest req) {
	    Passenger newPassenger = new Passenger();
	    newPassenger.setName(req.name);
	    newPassenger.setPhoneNo(req.phoneNum);
	    newPassenger.setEmailId(req.emailId);

	    return passRepo.save(newPassenger)
	            .flatMap(savedPassenger -> {
	                Address newAddress = new Address();
	                newAddress.setCity(req.city);
	                newAddress.setHouseNo(req.houseNo);
	                newAddress.setCountry(req.country);
	                newAddress.setState(req.state);
	                newAddress.setPassengerId(savedPassenger.getPassengerId());

	                return addRepo.save(newAddress)
	                        .map(savedAddress -> ResponseEntity.status(HttpStatus.CREATED)
	                                .body(savedPassenger.getPassengerId()));
	            })
	            .onErrorResume(ex -> {
	                if (ex.getMessage() != null && ex.getMessage().contains("E11000 duplicate key error")) {
	                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                            .body("Email already exists"));
	                }
	                return Mono.error(ex);
	            });
	}


	public Mono<ResponseEntity<List<Ticket>>> getTickets(String emailId) {
	    return passRepo.findByEmailId(emailId)  
	                   .switchIfEmpty(Mono.error(
	                       new ResourceNotFoundExceptionForResponseEntity(
	                           "Passenger with " + emailId + " not found"
	                       )
	                   ))
	                   .flatMap(passenger -> 
	                       tickRepo.findAllByPassengerId(passenger.getPassengerId()) 
	                               .collectList()  
	                               .map(tickets -> ResponseEntity.ok(tickets)) 
	                   );
	}

}
