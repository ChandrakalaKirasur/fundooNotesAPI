package com.bridgelabz.fundoonotes.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.dto.LabelDTO;
import com.bridgelabz.fundoonotes.entity.Label;
import com.bridgelabz.fundoonotes.entity.Note;
import com.bridgelabz.fundoonotes.response.Response;
import com.bridgelabz.fundoonotes.service.LabelService;

@RestController
public class LabelController {
	@Autowired
	private LabelService labelService;

	@PostMapping("/labels/create")
	public ResponseEntity<Response> createLabel(@RequestHeader String token,@RequestBody LabelDTO labelDto){
		Label createdLabel = labelService.createLabel(token, labelDto);
		if (createdLabel != null) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response(labelDto.getLabelName() + " created", 208, createdLabel));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(labelDto.getLabelName() + " got error while creating label", 400, labelDto.getLabelName()));
	}
	
	@PostMapping("/labels/createNote/{noteID}")
	public ResponseEntity<Response> createLabelForNote(@RequestHeader String token,@PathVariable("noteID") Long noteID,@RequestBody String labelDto){
		Label createdLabel = labelService.createLabelForNote(token, labelDto,noteID);
		if (createdLabel != null) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response(labelDto + " created for note", 208, createdLabel));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(labelDto + " got error while creating label", 400, labelDto));
	}

	@GetMapping("/lables/fetch/{noteID}")
	public ResponseEntity<Response> getAllLabelsOfNote(@RequestHeader String token,@PathVariable Long noteID) {
		List<Label> fetchedLabelsFromNote=labelService.getAllLabelsOfNote(token, noteID);
		if(fetchedLabelsFromNote!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("labels from note are: ",200,fetchedLabelsFromNote));
		}
		return ResponseEntity.status(HttpStatus.OK).body(new Response("no labels found for the note ",400,fetchedLabelsFromNote));
	}
	@GetMapping("/lables/fetch/all")
	public ResponseEntity<Response> getAllLabels(@RequestHeader String token) {
		List<Label> fetchedLabels=labelService.getAllLabels(token);
		if(fetchedLabels!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("labels are: ",200,fetchedLabels));
		}
		return ResponseEntity.status(HttpStatus.OK).body(new Response("no labels found for the note ",400,fetchedLabels));
	}
	@PutMapping("/labels/rename/{labelID}")
	public ResponseEntity<Response> updateLabel(@RequestHeader String token,@PathVariable Long labelID, @RequestBody String labelInformation) {
		Label updatedLabel = labelService.updateLabel(token, labelID, labelInformation);
		if (updatedLabel != null) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response(labelID + " updated successfully", 200, updatedLabel));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Error occured while updating label", 400, labelID));
	}

	@DeleteMapping("/labels/delete/{labelID}")
	public ResponseEntity<Response> deleteLabel(@RequestHeader String token,@PathVariable Long labelID) {
		boolean resultStatus = labelService.deleteLabelFromUser(token, labelID);
		if (resultStatus) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response(labelID + " deleted ", 200, resultStatus));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Error occured while deleting label", 400, labelID));
	}
	
	@PutMapping("/labels/map/{noteID}/{labelID}")
	public ResponseEntity<Response> mapLabelToNote(@RequestHeader String token, @PathVariable Long noteID,@PathVariable Long labelID) {
		Label mappedLabel = labelService.mapLabelToNote(token, noteID, labelID);
		if (mappedLabel != null) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response(labelID + " label added to note", 200, mappedLabel));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("oops!...error occured label", 400, labelID));
	}
	@PutMapping("/labels/unmap/{noteID}/{labelID}")
	public ResponseEntity<Response> unMapLabelToNote(@RequestHeader String token, @PathVariable Long noteID,@PathVariable Long labelID) {
		Label mappedLabel = labelService.unMapLabelToNote(token, noteID, labelID);
		if (mappedLabel != null) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response(labelID + " label removed from note", 200, mappedLabel));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("oops!...error occured in label", 400, labelID));
	}
	
	@GetMapping("/lables/fetchNote/{labelID}")
	public ResponseEntity<Response> getAllNotesOfLabel(@RequestHeader String token,@PathVariable Long labelID) {
		List<Note> fetchedNotesFromLabel=labelService.getAllNotesOfLabel(token,labelID);
		if(fetchedNotesFromLabel!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("labels from note are: ",200,fetchedNotesFromLabel));
		}
		return ResponseEntity.status(HttpStatus.OK).body(new Response("no labels found for the note ",400,fetchedNotesFromLabel));
	}
	@GetMapping("/lables/findLabel/{labelName}")
	public ResponseEntity<Response> getLabel(@RequestHeader String token,@PathVariable String labelName) {
		Label fetchedLabels=labelService.getLabel(token,labelName);
		if(fetchedLabels!=null) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("labels is ",200,fetchedLabels));
		}
		return ResponseEntity.status(HttpStatus.OK).body(new Response("no label present ",400,fetchedLabels));
	}
	
}
