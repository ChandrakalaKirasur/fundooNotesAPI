package com.bridgelabz.fundoonotes.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionHandlerResponse {
		private String message;
		private int code;
		private LocalDateTime time;
}
