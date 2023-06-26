package com.radovan.spring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.radovan.spring.exceptions.DataNotValidatedException;
import com.radovan.spring.exceptions.ExistingEmailException;
import com.radovan.spring.exceptions.InstanceNotExistException;
import com.radovan.spring.exceptions.InvalidUserException;

@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(InvalidUserException.class)
	public ResponseEntity<String> handleInvalidUserException(){
		return ResponseEntity.internalServerError().body("Invalid User!!!");
	}
	
	@ExceptionHandler(ExistingEmailException.class)
	public ResponseEntity<String> handleExistingEmailException(){
		return ResponseEntity.internalServerError().body("Email exists already!!!");
	}
	
	@ExceptionHandler(DataNotValidatedException.class)
	public ResponseEntity<String> handleDataNotValidatedException(){
		return ResponseEntity.internalServerError().body("Data is not validated!!!");
	}
	
	@ExceptionHandler(InstanceNotExistException.class)
	public ResponseEntity<String> handleInstanceNotExistException(){
		return ResponseEntity.internalServerError().body("Instance not exists!!!");
	}
}
