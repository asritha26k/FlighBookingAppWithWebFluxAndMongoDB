package com.flight.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TicketBookingRequest {

	@NotNull
	public String passenger_id;

	@NotBlank
	public String seatNo;
}
