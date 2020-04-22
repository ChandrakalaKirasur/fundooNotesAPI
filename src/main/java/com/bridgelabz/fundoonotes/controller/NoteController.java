package com.bridgelabz.fundoonotes.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.dto.NoteDTO;
import com.bridgelabz.fundoonotes.entity.Label;
import com.bridgelabz.fundoonotes.entity.Note;
import com.bridgelabz.fundoonotes.response.Response;
import com.bridgelabz.fundoonotes.service.NoteService;

@RestController
public class NoteController {
	@Autowired
	private NoteService noteService;

	// creating note by user
	@PostMapping("/create")
	public ResponseEntity<Response> createNote(@RequestHeader String token, @Valid @RequestBody NoteDTO noteDto){
		Note createdNote = noteService.createNote(noteDto, token);
		if (createdNote != null) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Note Created", 200, createdNote));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new Response("error creating new note", 208, noteDto.getTitle()));
	}

	@PutMapping("/updatenote")
	public ResponseEntity<Response> updateNotes(@RequestBody NoteDTO noteDto, @RequestParam("id") long noteId,
			@RequestHeader("token") String token) {
		Note updatedNote = noteService.updateNote(token, noteId, noteDto);
		if (updatedNote != null) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response(noteId + " note updated", 200, updatedNote));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new Response(" error updating note try again", 208, updatedNote));
	}

	@PutMapping("/remainder/{noteID}")
	public ResponseEntity<Response> setRemainder(@RequestHeader("token") String token, @PathVariable("noteID") Long noteId,
			@RequestBody String reminderDateAndTime) {
		noteService.setRemainderforNote(token, noteId, reminderDateAndTime);
		return ResponseEntity.status(HttpStatus.CREATED).body(new Response("remainder created", 201, noteId));
	}

	@PutMapping("/changecolor/{noteID}")
	public ResponseEntity<Response> changeColour(@PathVariable("noteID") long noteID,
			@RequestBody String color,@RequestHeader("token") String token) {
		noteService.changeColour(token, noteID, color);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("color changed", 200, noteID));
	}

	@DeleteMapping("/trash/{noteID}")
	public ResponseEntity<Response> trashNote(@RequestHeader("token") String token, @PathVariable("noteID") Long noteID) {
		if (noteService.trashNote(token, noteID))
			return ResponseEntity.status(HttpStatus.OK).body(new Response("note trashed", 200, noteID));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("note already trashed", 400, noteID));
	}

	@PutMapping("/pin/{noteID}")
	public ResponseEntity<Response> pinNote(@RequestHeader("token") String token, @PathVariable("noteID") Long noteID) {
		if (noteService.pinNote(noteID, token))
			return ResponseEntity.status(HttpStatus.OK).body(new Response("note pinned", 200, noteID));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("note already pinned", 400, noteID));
	}
	@PutMapping("/unpin/{noteID}")
	public ResponseEntity<Response> unPinNote(@RequestHeader("token") String token, @PathVariable("noteID") Long noteID) {
		if (noteService.unPinNote(noteID, token))
			return ResponseEntity.status(HttpStatus.OK).body(new Response("note unpinned", 200, noteID));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("note already pinned", 400, noteID));
	}
	@DeleteMapping("/archive/{noteID}")
	public ResponseEntity<Response> archiveNote(@PathVariable("noteID") long noteID,
			@RequestHeader("token") String token) {
		if (noteService.archiveNote(noteID, token))
			return ResponseEntity.status(HttpStatus.OK).body(new Response("note archieved", 200, noteID));
		return ResponseEntity.status(HttpStatus.OK).body(new Response("note unarchieved", 200, noteID));
	}
	
	@PutMapping("/remainder/remove/{noteID}")
	public ResponseEntity<Response> removeReminder(@RequestHeader("token") String token,
			@PathVariable("noteID") Long noteID) {
		noteService.removeReminderforNote(token, noteID);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("Reminder removed", 200, noteID));
	}

	/* to read all note information */
	@GetMapping("/fetch/notes")
	public ResponseEntity<Response> getAllNotes(@RequestHeader String token) {
		List<Note> fetchedNotes= noteService.getAllNotes(token);
		if(fetchedNotes!=null){
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Notes are", 200, fetchedNotes));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Oops!!notes not found", 208,""));
			
	}

	/*
	 * to read all user information it will takes parameters as note id to search
	 * note in database
	 */
	@GetMapping("/note/{noteID}")
	public ResponseEntity<Response> searchNoteByNoteID(@PathVariable Long noteID,
			@RequestHeader("token") String token) {
		Note fetchedNote = noteService.searchNote(noteID, token);
		if (fetchedNote != null) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Searched note is", 200, fetchedNote));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Note doesn't exist", 208, noteID));
	}

	@GetMapping("/fetch/trashed/notes")
	public ResponseEntity<Response> fetchTrashedNotes(@RequestHeader("token") String token) {
		List<Note> trashedNotes = noteService.getAllTrashedNotes(token);
		if (!trashedNotes.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Trashed notes are", 200, trashedNotes));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Trashed notes not found", 404, ""));
	}

	@GetMapping("/fetch/notes/labels/{noteID}")
	public ResponseEntity<Response> fetchLabelsOfNote(@RequestHeader("token") String token,
			@PathVariable("noteID") long noteID) {
		List<Label> fetchedLabels = noteService.getLabelsOfNote(token, noteID);
		if (!fetchedLabels.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Labels are", 200, fetchedLabels));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Label not found for note", 404, ""));
	}

	@GetMapping("fetch/pinned/notes")
	public ResponseEntity<Response> fetchPinnedNotes(@RequestHeader("token") String token) {
		List<Note> pinnedNotes = noteService.getAllPinnedNotes(token);
		if (!pinnedNotes.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Pinned notes are", 200, pinnedNotes));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Notes not found", 404, ""));
	}

	@GetMapping("/fetch/archived/notes")
	public ResponseEntity<Response> fetchArchivedNotes(@RequestHeader("token") String token) {
		List<Note> archivedNotes = noteService.getAllArchivedNotes(token);
		if (!archivedNotes.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Archived notes are", 200, archivedNotes));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Notes not found", 404, ""));
	}
	
	@GetMapping("/fetch/reminder/notes")
	public ResponseEntity<Response> fetchReminderNotes(@RequestHeader("token") String token) {
		List<Note> reminderNotes = noteService.getAllReminderNotes(token);
		if (!reminderNotes.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Reminder notes are", 200, reminderNotes));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Notes not found", 404, ""));
	}
	
	@GetMapping("notes/{sortByTitle}")
	public ResponseEntity<Response> searchByTitle(@RequestHeader("token") String token, @PathVariable String title) {
		List<Note> searchedNotes = noteService.searchByTitle(token, title);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new Response("similar notes to your search", 200, searchedNotes));
	}

	@GetMapping("{id}/{sortByDate}")
	public ResponseEntity<Response> searchNoteByDate(@RequestHeader("token") String token) {
		List<Note> searchedNotes = noteService.searchNoteByDate(token);
		if (searchedNotes != null)
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Sorted notes", 200, searchedNotes));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Notes not found", 400, ""));
	}

	// deleting notes by user
	@DeleteMapping("/deleteNote/{noteID}")
	public ResponseEntity<Response> deleteNotes(@RequestHeader String token, @PathVariable Long noteID) {
		boolean resultStatus = noteService.deleteNotes(token, noteID);
		if (resultStatus) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response(noteID + " deleted", 200, resultStatus));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new Response("got error while deleting this note", 208, resultStatus));
	}
	@PutMapping("/restoreNote/{noteID}")
	public ResponseEntity<Response> restoreNotes(@RequestHeader String token, @PathVariable Long noteID) {
		boolean resultStatus = noteService.restoreNotes(token, noteID);
		if (resultStatus) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response(noteID + " restored", 200, resultStatus));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new Response("got error while restoring this note", 208, resultStatus));
	}
}