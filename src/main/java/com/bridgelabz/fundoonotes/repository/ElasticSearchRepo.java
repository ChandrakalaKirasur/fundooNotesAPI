package com.bridgelabz.fundoonotes.repository;

import java.util.List;

import com.bridgelabz.fundoonotes.entity.Note;

public interface ElasticSearchRepo{

	void createNote(Note note);

	String updateNote(Note note);

	String deleteNote(Note note);

	List<Note> searchByTitle(String title);

}
