package com.bridgelabz.fundoonotes.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author Chandrakala Kirasur
 *
 */
@Document(type="note",shards=2, indexName = "notes")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Note implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long noteID;
	@NotNull
	private String title;
	private String description;

	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	private String reminderDate;
	private String color;
	@Column(name = "trashed", columnDefinition = "boolean default false")
	private boolean trashed;
	@Column(name = "pinned", columnDefinition = "boolean default false")
	private boolean pinned;
	@Column(name = "archieved", columnDefinition = "boolean default false")
	private boolean archieved;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "notesList")
	private List<Label> labels;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "collaboratedNotes")
	private List<User> collaboratedUsers;

	public Long getNoteID() {
		return noteID;
	}

	public void setNoteID(Long noteID) {
		this.noteID = noteID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getReminderDate() {
		return reminderDate;
	}

	public void setReminderDate(String reminderDate) {
		this.reminderDate = reminderDate;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public boolean isTrashed() {
		return trashed;
	}

	public void setTrashed(boolean trashed) {
		this.trashed = trashed;
	}

	public boolean isPinned() {
		return pinned;
	}

	public void setPinned(boolean pinned) {
		this.pinned = pinned;
	}

	public boolean isArchieved() {
		return archieved;
	}

	public void setArchieved(boolean archieved) {
		this.archieved = archieved;
	}

	public List<Label> getLabels() {
		return labels;
	}

	public void setLabels(List<Label> labels) {
		this.labels = labels;
	}

	public List<User> getCollaboratedUsers() {
		return collaboratedUsers;
	}

	public void setCollaboratedUsers(List<User> collaboratedUsers) {
		this.collaboratedUsers = collaboratedUsers;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}