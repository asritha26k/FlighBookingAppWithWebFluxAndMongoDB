package com.flight.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.flight.entity.Address;

public interface AddressRepository extends ReactiveMongoRepository<Address, String> {

}
