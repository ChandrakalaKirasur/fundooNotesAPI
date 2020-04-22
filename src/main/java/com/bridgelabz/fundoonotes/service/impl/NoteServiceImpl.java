package com.bridgelabz.fundoonotes.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.bridgelabz.fundoonotes.dto.NoteDTO;
import com.bridgelabz.fundoonotes.entity.Label;
import com.bridgelabz.fundoonotes.entity.Note;
import com.bridgelabz.fundoonotes.entity.User;
import com.bridgelabz.fundoonotes.exception.NoteException;
import com.bridgelabz.fundoonotes.exception.UserException;
import com.bridgelabz.fundoonotes.repository.ElasticSearchRepo;
import com.bridgelabz.fundoonotes.repository.NoteRepository;
import com.bridgelabz.fundoonotes.repository.UserRepository;
import com.bridgelabz.fundoonotes.service.NoteService;
import com.bridgelabz.fundoonotes.utility.JWTUtil;

@Service
public class NoteServiceImpl implements NoteService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private NoteRepository noteRepo;
	@Autowired
	private JWTUtil util;
	@Autowired
	private ElasticSearchRepo elasticSearch;

	/*
	 * checks the user token is valid or not. If true then it checks whether note is
	 * already exist if not then it will adds new note to the database
	 */
	@Override
	public Note createNote(NoteDTO notedto, String token) {
		User user = authenticatedUser(token);
		Note note = new Note();
		BeanUtils.copyProperties(notedto, note);
		note.setCreatedDate(LocalDateTime.now());
		note.setUpdatedDate(LocalDateTime.now());
		user.getNotes().add(note);
		noteRepo.save(note);
		userRepo.save(user);
		elasticSearch.createNote(note);
		return note;
	}

	/*
	 * this method will returns all the notes as a list from the user account if
	 * user exists or else throws user exception
	 */
	@Override
	public List<Note> getAllNotes(String token) {
		User user = authenticatedUser(token);
		return user.getNotes().stream().filter(p -> p.isTrashed() == false && p.isArchieved()==false).collect(Collectors.toList());
	}

	/*
	 * It will takes the token and check whether users token is valid or not. If
	 * token is valid, note will be updated and sets the updated time
	 */
	@Override
	public Note updateNote(String token, Long noteID, NoteDTO noteDto) {
		User user = authenticatedUser(token);
		Note note = isVerifiedNote(user, noteID);
		BeanUtils.copyProperties(noteDto, note);
		note.setUpdatedDate(LocalDateTime.now());
		user.getNotes().add(note);
		userRepo.save(user);
		System.out.println(elasticSearch.updateNote(note));
		return note;
	}

	/*
	 * This method will takes token and if it is valid then it will deletes the note
	 * by referring its id otherwise throws defined exception
	 */
	@Override
	public boolean deleteNotes(String token, @PathVariable Long noteID) {
		User fetchedUser = authenticatedUser(token);
		if (util.validateToken(token, fetchedUser)) {
			Note note = isVerifiedNote(fetchedUser, noteID);
			fetchedUser.getNotes().remove(note);
			userRepo.save(fetchedUser);
			elasticSearch.deleteNote(note);
			return true;
		}
		return false;
	}

	/*
	 * This method will take note Id and user token. It will verifies both user and
	 * note if they are valid it will makes the note as pinned boolean true. Then it
	 * will response back as boolean true or false.
	 */
	@Override
	public boolean pinNote(Long noteId, String token) {
		User fetchedUser = authenticatedUser(token);
		Note fetchedNote = isVerifiedNote(fetchedUser, noteId);
		if (!fetchedNote.isPinned()) {
			fetchedNote.setPinned(true);
			fetchedNote.setUpdatedDate(LocalDateTime.now());
			fetchedUser.getNotes().add(fetchedNote);
			userRepo.save(fetchedUser);
			return true;
		}
		fetchedNote.setUpdatedDate(LocalDateTime.now());
		fetchedUser.getNotes().add(fetchedNote);
		userRepo.save(fetchedUser);
		return false;
	}

	/*
	 * This method will take note Id and user token. It will verifies both user and
	 * note if they are valid it will makes the note as pinned boolean true. Then it
	 * will response back as boolean true or false.
	 */
	@Override
	public boolean trashNote(String token, Long noteId) {
		User fetchedUser = authenticatedUser(token);
		Note fetchedNote = isVerifiedNote(fetchedUser, noteId);
		if (!fetchedNote.isTrashed()) {
			fetchedNote.setTrashed(true);
			fetchedNote.setReminderDate(null);
			fetchedNote.setPinned(false);
			fetchedNote.setUpdatedDate(LocalDateTime.now());
			fetchedUser.getNotes().add(fetchedNote);
			userRepo.save(fetchedUser);
			return true;
		}
		return false;
	}

	/*
	 * This method will take note Id and user token. It will verifies both user and
	 * note if they are valid it will makes the note as archived boolean true. Then
	 * it will response back as boolean true or false.
	 */
	@Override
	public boolean archiveNote(Long noteId, String token) {
		User fetchedUser = authenticatedUser(token);
		Note fetchedNote = isVerifiedNote(fetchedUser, noteId);
		if (!fetchedNote.isArchieved()) {
			fetchedNote.setArchieved(true);
			fetchedNote.setUpdatedDate(LocalDateTime.now());
			fetchedUser.getNotes().add(fetchedNote);
			userRepo.save(fetchedUser);
			return true;
		}
		fetchedNote.setArchieved(false);
		fetchedNote.setUpdatedDate(LocalDateTime.now());
		fetchedUser.getNotes().add(fetchedNote);
		userRepo.save(fetchedUser);
		return false;
	}

	/*
	 * This method will takes user token, note Id and reminder time which java
	 * LocalDateTime. It will verifies user and note then it will add reminder time
	 * to the note and updates the note to the user repository otherwise it will
	 * throws exception which will be handled by ExceptionHandler response classes.
	 */
	@Override
	public void setRemainderforNote(String token, Long noteId, String reminderDateAndTime) {
		User fetchedUser = authenticatedUser(token);
		Note fetchedNote = isVerifiedNote(fetchedUser, noteId);
		if (!fetchedNote.isTrashed()) {
			fetchedNote.setUpdatedDate(LocalDateTime.now());
			char[] ca=reminderDateAndTime.toCharArray();
			String remind=reminderDateAndTime.substring(1, reminderDateAndTime.length()-1);
			String reminderDate="";
			for(int i=1;i<ca.length-1;i++) {
				reminderDate +=ca[i];
			}
			System.out.println(reminderDate);
			System.out.println(remind);
			fetchedNote.setReminderDate(reminderDate);
			fetchedUser.getNotes().add(fetchedNote);
			userRepo.save(fetchedUser);
			return;
		}
		throw new NoteException("Opps...Remainder already set!", 502);
	}

	/*
	 * This method will takes token and note Id. Then it will verifies the user and
	 * note, if they both are valid it will remove and reminder date and updates the
	 * note with user repository. Otherwise it will throws exception.
	 */
	@Override
	public void removeReminderforNote(String token, Long noteId) {
		User fetchedUser = authenticatedUser(token);
		Note fetchedNote = isVerifiedNote(fetchedUser, noteId);
		if (fetchedNote.getReminderDate() != null) {
			fetchedNote.setReminderDate(null);
			fetchedNote.setUpdatedDate(LocalDateTime.now());
			fetchedUser.getNotes().add(fetchedNote);
			userRepo.save(fetchedUser);
			return;
		}
		throw new NoteException("Opps...Remainder already removed!", 502);
	}

	/*
	 * This method will gives all labels of a note from the user repository by
	 * taking user token and note ID as input
	 */
	@Override
	public List<Label> getLabelsOfNote(String token, Long noteId) {
		User fetchedUser = authenticatedUser(token);
		Note note = isVerifiedNote(fetchedUser, noteId);
		return note.getLabels();
	}

	/*
	 * This method will takes token of user and ID of a note and new color to
	 * change. If user exists and that note present in user repository then it will
	 * updates the note and stores in user object. If user and note doesn't exists
	 * it will throws exception for respective problem
	 */
	@Override
	public void changeColour(String token, Long noteId, String noteColour) {
		User fetchedUser = authenticatedUser(token);
		Note fetchedNote = isVerifiedNote(fetchedUser, noteId);
		fetchedNote.setColor(noteColour);
		fetchedNote.setUpdatedDate(LocalDateTime.now());
		fetchedUser.getNotes().add(fetchedNote);
		userRepo.save(fetchedUser);
	}

	/*
	 * This method will gives all trashed notes from the user repository by taking
	 * user token as a input
	 */
	@Override
	public List<Note> getAllTrashedNotes(String token) {
		List<Note> fetchedTrashedNotes = userRepo.getAllTrashedNotes(authenticatedUser(token).getUserID());
		if (!fetchedTrashedNotes.isEmpty()) {
			return fetchedTrashedNotes;
		}
		return fetchedTrashedNotes;
	}

	/*
	 * This method will gives all archived notes from the user repository by taking
	 * user token as a input
	 */
	@Override
	public List<Note> getAllArchivedNotes(String token) {
		User fetchedUser = authenticatedUser(token);
		List<Note> fetchedArchivedNotes = userRepo.getAllArchivedNotes(fetchedUser.getUserID());
		if (!fetchedArchivedNotes.isEmpty()) {
			return fetchedArchivedNotes;
		}
		return fetchedArchivedNotes;
	}

	/*
	 * This method will gives all pinned notes from the user repository by taking
	 * user token as a input
	 */
	@Override
	public List<Note> getAllPinnedNotes(String token) {
		User fetchedUser = authenticatedUser(token);
		return userRepo.findAllPinnedNotes(fetchedUser.getUserID());
	}

	/*
	 * This method is used to verify the user and fetch by using token as a string
	 */
	public User authenticatedUser(String token) {
		return userRepo.findByUserID(util.extractUserID(token))
				.orElseThrow(() -> new UserException("user not found", 400));
	}

	/*
	 * This method is used for verify note whether it is related to user or not if
	 * yes it will sends the optional note to user.
	 */
	@Override
	public Note isVerifiedNote(User user, Long noteID) {
		List<Note> noteRecords = user.getNotes();
		for (Note n : noteRecords) {
			if (n.getNoteID() == noteID)
				return n;
		}
		throw new NoteException("note not found", 400);
	}

	@Override
	public Note searchNote(Long noteID, String token){
		User user = authenticatedUser(token);
		Note note = null;
		if (util.validateToken(token, user)) {
			note = isVerifiedNote(user, noteID);
			return note;
		}
		return note;
	}

	@Override
	public List<Note> searchByTitle(String token, String title) {
		return elasticSearch.searchByTitle(title);
	}

	@Override
	public List<Note> searchNoteByDate(String token) {
		User fetchedUser = authenticatedUser(token);
		List<Note> fetchedNotes=fetchedUser.getNotes();
		if(fetchedNotes!=null) {
			fetchedNotes.sort((Note note1,Note note2)->note1.getCreatedDate().compareTo(note2.getCreatedDate()));
			return fetchedNotes;
		}
		return fetchedNotes;
	}

	@Override
	public boolean restoreNotes(String token, Long noteID) {
		User fetchedUser = authenticatedUser(token);
		Note fetchedNote = isVerifiedNote(fetchedUser, noteID);
		if (fetchedNote.isTrashed()) {
			fetchedNote.setTrashed(false);
			fetchedNote.setUpdatedDate(LocalDateTime.now());
			fetchedUser.getNotes().add(fetchedNote);
			userRepo.save(fetchedUser);
			return true;
		}
		return false;
	}

	@Override
	public boolean unPinNote(Long noteID, String token) {
		User fetchedUser = authenticatedUser(token);
		Note fetchedNote = isVerifiedNote(fetchedUser, noteID);
		if (fetchedNote.isPinned()) {
			fetchedNote.setPinned(false);
			fetchedNote.setUpdatedDate(LocalDateTime.now());
			fetchedUser.getNotes().add(fetchedNote);
			userRepo.save(fetchedUser);
			return true;
		}
		fetchedNote.setUpdatedDate(LocalDateTime.now());
		fetchedUser.getNotes().add(fetchedNote);
		userRepo.save(fetchedUser);
		return false;
	}

	@Override
	public List<Note> getAllReminderNotes(String token) {
		User fetchedUser = authenticatedUser(token);
		return userRepo.findAllReminderNotes(fetchedUser.getUserID());
	}
	
}
