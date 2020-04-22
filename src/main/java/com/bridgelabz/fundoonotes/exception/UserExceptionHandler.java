package com.bridgelabz.fundoonotes.exception;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.bridgelabz.fundoonotes.response.ExceptionHandlerResponse;
@ControllerAdvice
public class UserExceptionHandler extends ResponseEntityExceptionHandler{
	@ExceptionHandler(UserException.class)
	public final ResponseEntity<ExceptionHandlerResponse> userException(UserException ex){
		ExceptionHandlerResponse exp=new ExceptionHandlerResponse();
		exp.setMessage(ex.getStatusMessage());
		exp.setCode(ex.getStatusCode());
		exp.setTime(LocalDateTime.now());
		return ResponseEntity.status(exp.getCode()).body(new ExceptionHandlerResponse(exp.getMessage(), exp.getCode(), exp.getTime()));
	}
}
