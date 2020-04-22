package com.bridgelabz.fundoonotes.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.entity.User;
@Service
public interface CollaboratorService {

	boolean addColaborator(String token, Long noteId, String emailId);

	List<User> getColaboratorsOfNote(String token, Long noteId);

	boolean removeColaborator(String token, Long noteId, String emailId);

}
