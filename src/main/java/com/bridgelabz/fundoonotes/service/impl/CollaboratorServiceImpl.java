package com.bridgelabz.fundoonotes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.entity.Note;
import com.bridgelabz.fundoonotes.entity.User;
import com.bridgelabz.fundoonotes.exception.CollaboratorException;
import com.bridgelabz.fundoonotes.exception.NoteException;
import com.bridgelabz.fundoonotes.exception.UserException;
import com.bridgelabz.fundoonotes.repository.UserRepository;
import com.bridgelabz.fundoonotes.service.CollaboratorService;
import com.bridgelabz.fundoonotes.utility.JWTUtil;
@Service
public class CollaboratorServiceImpl implements CollaboratorService{
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private JWTUtil jwtUtil;
	
	private User authenticatedMainUser(String token) {
		User fetchedUser = userRepo.findByUserID(jwtUtil.extractUserID(token)).orElseThrow(()->new UserException("User Authentication exception",400));
		return fetchedUser;
		}
	private Note verifiedNote(long noteID,User fetchedUser) {
		Note fetchedNote = fetchedUser.getNotes().stream().filter(n->n.getNoteID().equals(noteID)).findFirst().orElseThrow(()-> new NoteException("note not found",400));
		if (!fetchedNote.isTrashed()) {
			return fetchedNote;
		}
		throw new NoteException("Opps...Note is trashed!", 404);
	}

	private User validColaborator(String emailId) {
	User fetchedColaborator = userRepo.findByEmailId(emailId).orElseThrow(()->new UserException("Collaborator is not valid user",400));
	if (fetchedColaborator.isVerified()) {
	return fetchedColaborator;
	}
	throw new CollaboratorException("Opps...Collaborator is not valid user!", 404);
	}

	@Override
	public boolean addColaborator(String token, Long noteId, String emailId) {
		User fetchedUser=authenticatedMainUser(token);
	if (fetchedUser.getEmailId().equals(emailId)) {
		throw new CollaboratorException("Opps...Can't add your own account as collaborator", 400);
	}
	Note fetchedValidNote = verifiedNote(noteId,fetchedUser);
	User fetchedValidColaborator = validColaborator(emailId);
	fetchedValidNote.getCollaboratedUsers().add(fetchedValidColaborator);
	fetchedValidColaborator.getCollaboratedNotes().add(fetchedValidNote);
	userRepo.save(fetchedValidColaborator);
	return true;
	}

	@Override
	public List<User> getColaboratorsOfNote(String token, Long noteID) {
	User fetchedUser=authenticatedMainUser(token);
	return verifiedNote(noteID,fetchedUser).getCollaboratedUsers();
	}

	@Override
	public boolean removeColaborator(String token, Long noteID, String emailID) {
	User fetchedUser=authenticatedMainUser(token);
	Note fetchedValidNote = verifiedNote(noteID,fetchedUser);
	User fetchedValidColaborator = validColaborator(emailID);
	fetchedValidNote.getCollaboratedUsers().remove(fetchedValidColaborator);
	fetchedValidColaborator.getCollaboratedNotes().remove(fetchedValidNote);
	userRepo.save(fetchedValidColaborator);
	return true;

	} 
}
