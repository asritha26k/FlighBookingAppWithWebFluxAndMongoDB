package com.flight.request;

import jakarta.validation.constraints.NotBlank;

public class SearchReq {

	@NotBlank
	public String origin;

	@NotBlank
	public String destination;
}
