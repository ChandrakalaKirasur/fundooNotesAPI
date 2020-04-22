package com.bridgelabz.fundoonotes.exception;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.bridgelabz.fundoonotes.response.ExceptionHandlerResponse;

@ControllerAdvice
public class NoteExceptionHandler extends ResponseEntityExceptionHandler{
	@ExceptionHandler(NoteException.class)
	public final ResponseEntity<ExceptionHandlerResponse> noteException(NoteException ex){
		ExceptionHandlerResponse exp=new ExceptionHandlerResponse();
		exp.setMessage(ex.getStatusMessage());
		exp.setCode(ex.getStatusCode());
		exp.setTime(LocalDateTime.now());
		return ResponseEntity.status(exp.getCode()).body(new ExceptionHandlerResponse(exp.getMessage(), exp.getCode(), exp.getTime()));
	}
}
