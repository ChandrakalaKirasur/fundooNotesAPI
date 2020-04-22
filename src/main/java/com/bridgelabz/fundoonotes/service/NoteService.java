package com.bridgelabz.fundoonotes.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.dto.NoteDTO;
import com.bridgelabz.fundoonotes.entity.Label;
import com.bridgelabz.fundoonotes.entity.Note;
import com.bridgelabz.fundoonotes.entity.User;

@Service
public interface NoteService {

	public Note createNote(NoteDTO note, String token);

	public boolean deleteNotes(String token, Long noteID);

	Note updateNote(String token, Long noteID, NoteDTO noteDto);

	public void changeColour(String token, Long noteId, String noteColour);

	void setRemainderforNote(String token, Long noteId, String reminderDateAndTime);

	// this method is for label crud operations
	public User authenticatedUser(String token);

	public Note isVerifiedNote(User user, Long noteID);

	boolean pinNote(Long noteId, String token);

	public boolean archiveNote(Long noteId, String token);

	public boolean trashNote(String token, Long noteId);

	public Note searchNote(Long noteID, String token);

	public List<Note> searchByTitle(String token, String title);

	public List<Note> searchNoteByDate(String token);

	List<Note> getAllNotes(String token);

	public List<Note> getAllPinnedNotes(String token);

	public List<Note> getAllArchivedNotes(String token);

	public List<Note> getAllTrashedNotes(String token);

	public List<Label> getLabelsOfNote(String token, Long noteId);
	
	public void removeReminderforNote(String token, Long noteId);

	public boolean restoreNotes(String token, Long noteID);

	public boolean unPinNote(Long noteID, String token);

	public List<Note> getAllReminderNotes(String token);


}
