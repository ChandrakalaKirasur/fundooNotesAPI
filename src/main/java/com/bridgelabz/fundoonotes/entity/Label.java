package com.bridgelabz.fundoonotes.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author Chandrakala Kirasur
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Label {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long labelID;
	private String labelName;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	
	@ManyToMany
	@JoinColumn(name = "labelid")
	private List<Note> notesList;
	
	public List<Note> getNotesList(){
		return this.notesList;
	}
	public Long getLabelID() {
		return labelID;
	}
	public void setLabelID(Long labelID) {
		this.labelID = labelID;
	}
	public String getLabelName() {
		return labelName;
	}
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}
	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}
	public void setNotesList(List<Note> notesList) {
		this.notesList = notesList;
	}
	
}
