package com.flight;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.flight.exception.ResourceNotFoundExceptionForResponseEntity;

@RestControllerAdvice // this lets use to use these exceptions globally
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationErrors(MethodArgumentNotValidException ex) {
		Map<String, String> mp = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach((error) -> {
			mp.put(error.getField(), error.getDefaultMessage());
		});
		return mp;
	}

	@ExceptionHandler(ResourceNotFoundExceptionForResponseEntity.class)
	public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundExceptionForResponseEntity ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
}
