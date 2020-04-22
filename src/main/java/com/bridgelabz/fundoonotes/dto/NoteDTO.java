package com.bridgelabz.fundoonotes.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NoteDTO {
	private String title;
	private String description;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	private String color;
	private boolean verified;
	
}
