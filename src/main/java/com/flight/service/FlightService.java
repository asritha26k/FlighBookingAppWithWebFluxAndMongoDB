package com.flight.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.flight.entity.Flight;
import com.flight.exception.ResourceNotFoundExceptionForResponseEntity;
import com.flight.repository.FlightRepository;
import com.flight.request.SearchReq;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FlightService {
	@Autowired
	FlightRepository flightRepo;

	public Mono<ResponseEntity<String>> addService(Flight flight) {

		Mono<Flight> savedFlight = flightRepo.save(flight);
		return savedFlight.map(f->ResponseEntity.status(HttpStatus.CREATED).body(f.getFlightId()));
		
	}

	public Mono<ResponseEntity<List<Flight>>> searchService(SearchReq searchReq) throws ResourceNotFoundExceptionForResponseEntity {
	    String from = searchReq.origin;
	    String to = searchReq.destination;

	    return flightRepo.findByOriginAndDestination(from, to) 
	                     .collectList()                        
	                     .flatMap(list -> {
	                         if (list.isEmpty()) {
	                             return Mono.error(new ResourceNotFoundExceptionForResponseEntity(
	                                 "No flights found from " + from + " to " + to
	                             ));
	                         }
	                         return Mono.just(ResponseEntity.ok(list));
	                     });
	}

	public Mono<ResponseEntity<Void>> deleteFlightService(String i) throws ResourceNotFoundExceptionForResponseEntity {
	    return flightRepo.findById(i)
	                     .switchIfEmpty(Mono.error(
	                         new ResourceNotFoundExceptionForResponseEntity(
	                             "Flight with ID " + i + " not found"
	                         )
	                     ))
	                     .flatMap(flight -> flightRepo.delete(flight)  // delete returns Mono<Void>
	                                               .then(Mono.just(ResponseEntity.ok().build()))
	                     );
	}



	public Mono<Flight> getByFlightId(int flightId) {
		return flightRepo.findByflightId(flightId);

	}

}
