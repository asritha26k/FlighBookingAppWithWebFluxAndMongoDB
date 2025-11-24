package com.flight.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PassengerRequest {

	@NotBlank
	public String name;

	@NotBlank
	@Email
	public String emailId;

	@NotNull
	@Positive
	public Long phoneNum;

	@NotNull
	@Positive
	public Integer houseNo;

	@NotBlank
	public String city;

	@NotBlank
	public String state;

	@NotBlank
	public String country;

}
