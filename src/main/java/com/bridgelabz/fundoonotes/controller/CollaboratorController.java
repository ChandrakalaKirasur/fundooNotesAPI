package com.bridgelabz.fundoonotes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.entity.User;
import com.bridgelabz.fundoonotes.response.Response;
import com.bridgelabz.fundoonotes.service.CollaboratorService;
@RestController
public class CollaboratorController {
	@Autowired
	private CollaboratorService colaboratorService;

	@PostMapping("/addCollaborator/{noteID}/{emailID}")
	public ResponseEntity<Response> addColaborator(@RequestHeader("token") String token,@PathVariable("noteID") Long noteID, @PathVariable("emailID") String emailID) {
	if (colaboratorService.addColaborator(token, noteID, emailID)) {
	return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response("colaborator added.", 202, emailID));
	}
	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	.body(new Response("Opps...Error adding colborator!", 400,""));

	}

	@GetMapping("/getCollaborators/{noteID}")
	public ResponseEntity<Response> getColaborators(@RequestHeader("token") String token,@PathVariable("noteID") Long noteID) {
	List<User> fetchedColaborators = colaboratorService.getColaboratorsOfNote(token, noteID);
	if (!fetchedColaborators.isEmpty()) {
	return ResponseEntity.status(HttpStatus.OK)
	.body(new Response("colaborators are", 200, fetchedColaborators));
	}
	return ResponseEntity.status(HttpStatus.NOT_FOUND)
	.body(new Response("collaborators not found for this note", 404,""));
	}

	@DeleteMapping("/deleteCollaborator/{noteId}/{emailID}")
	public ResponseEntity<Response> deleteColaborator(@RequestHeader("token") String token,
	@PathVariable("noteId") long noteId, @PathVariable("emailID") String emailID) {
	if (!colaboratorService.removeColaborator(token, noteId, emailID)) {
	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	.body(new Response("Opps...Error removing Collaborator!", 400,""));
	}
	return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response("colaborator removed.", 202, emailID));
	}
}
