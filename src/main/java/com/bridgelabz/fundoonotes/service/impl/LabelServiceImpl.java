package com.bridgelabz.fundoonotes.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.dto.LabelDTO;
import com.bridgelabz.fundoonotes.entity.Label;
import com.bridgelabz.fundoonotes.entity.Note;
import com.bridgelabz.fundoonotes.entity.User;
import com.bridgelabz.fundoonotes.exception.LabelException;
import com.bridgelabz.fundoonotes.repository.LabelRepository;
import com.bridgelabz.fundoonotes.repository.NoteRepository;
import com.bridgelabz.fundoonotes.repository.UserRepository;
import com.bridgelabz.fundoonotes.service.LabelService;
import com.bridgelabz.fundoonotes.service.NoteService;

@Service
public class LabelServiceImpl implements LabelService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private LabelRepository labelRepo;
	@Autowired
	private NoteRepository noteRepo;
	@Autowired
	private NoteService noteService;

	// got label object and saving that into list
	@Override
	public Label createLabel(String token,LabelDTO labelDto) {
		User user = noteService.authenticatedUser(token);
		verifyLabel(labelDto, user);
		Label label=new Label();
		BeanUtils.copyProperties(labelDto, label);
		label.setCreatedDate(LocalDateTime.now());
		label.setUpdatedDate(LocalDateTime.now());
		user.getLabels().add(label);
		labelRepo.save(label);
		userRepo.save(user);
		return label;
	}

	// this method will returns all labels to the controller

	@Override
	public List<Label> getAllLabelsOfNote(String token, Long noteID) {
		User fetchedUser = noteService.authenticatedUser(token);
		Note fetchedNote=noteService.isVerifiedNote(fetchedUser, noteID);
		return fetchedNote.getLabels();
	}

	@Override
	public Label updateLabel(String token, Long labelID,String labelInformation) {
		User user = noteService.authenticatedUser(token);
		Label label = user.getLabels().stream().filter(l->l.getLabelID().equals(labelID)).findFirst().orElseThrow(()->new LabelException("label not found in user",404));
		label.setLabelName(labelInformation);
		user.getLabels().add(label);
		labelRepo.save(label);
		userRepo.save(user);
		return label;
	}

	@Override
	public boolean deleteLabelFromUser(String token, Long labelID) {
		User user = noteService.authenticatedUser(token);
		Label label=user.getLabels().stream().filter(l->l.getLabelID().equals(labelID)).findFirst().orElseThrow(()->new LabelException("label not found in user",404));
		List<Note> notes=user.getNotes();
		Label newLabel=null;Note newNote=null;
		for(Note n:notes) {
			List<Label> labels=n.getLabels();
			System.out.println("labels of note"+n.getLabels());
			if(labels!=null) {
			for(Label l:labels) {
				if(l.getLabelID().equals(labelID)) {
					newLabel=l;
					newNote=n;
				}
			}
			}
		}
		boolean status=user.getLabels().remove(label);
		userRepo.save(user);
		labelRepo.delete(newLabel);
		try {
		newNote.getLabels().remove(newLabel);
		noteRepo.save(newNote);
		}
		catch(Exception e) {
			return status;
		}
		return status;
		
	}

	@Override
	public List<Label> getAllLabels(String token) {
		User fetchedUser = noteService.authenticatedUser(token);
		return fetchedUser.getLabels();
	}

	@Override
	public Label mapLabelToNote(String token, Long noteID, Long labelID) {
		User user = noteService.authenticatedUser(token);
		Note fetchedNote=noteService.isVerifiedNote(user, noteID);
		Label label=user.getLabels().stream().filter(l->l.getLabelID().equals(labelID)).findFirst().orElseThrow(()->new LabelException("label not found in user",404));
		fetchedNote.getLabels().add(label);
		System.out.println(label.getNotesList());
		label.getNotesList().add(fetchedNote);
		user.getNotes().add(fetchedNote);
		noteRepo.save(fetchedNote);
		userRepo.save(user);
		labelRepo.save(label);
		return label;
	}

	@Override
	public Label unMapLabelToNote(String token, Long noteID, Long labelID) {
		User user = noteService.authenticatedUser(token);
		Note fetchedNote=noteService.isVerifiedNote(user, noteID);
		Label label=user.getLabels().stream().filter(l->l.getLabelID().equals(labelID)).findFirst().orElseThrow(()->new LabelException("label not found in user",404));
		fetchedNote.getLabels().remove(label);
		label.getNotesList().remove(fetchedNote);
		labelRepo.save(label);
		user.getNotes().add(fetchedNote);
		noteRepo.save(fetchedNote);
		userRepo.save(user);
		return label;
	}

	@Override
	public List<Note> getAllNotesOfLabel(String token, Long labelID) {
		User user = noteService.authenticatedUser(token);
		Label label=user.getLabels().stream().filter(l->l.getLabelID().equals(labelID)).findFirst().orElseThrow(()->new LabelException("label not found in user",404));
		return label.getNotesList();
	}

	@Override
	public Label getLabel(String token, String labelName) {
		User user = noteService.authenticatedUser(token);
		return user.getLabels().stream().filter(l->l.getLabelName().equals(labelName)).findFirst().orElseThrow(()->new LabelException("label not found in user",404));
	}

	@Override
	public Label createLabelForNote(String token, String labelDto, long noteID) {
		LabelDTO lab=new LabelDTO(labelDto);
		Label label=createLabel(token,lab);
		System.out.println(label.getLabelID());
		mapLabelToNote(token,noteID,label.getLabelID());
		return label;
	}
	public void verifyLabel(LabelDTO labelDto,User user) {
		List<Label> fetchedLabels=user.getLabels();
		Optional<Label> status=fetchedLabels.stream().filter(l-> l.getLabelName().equals(labelDto.getLabelName())).findFirst();
		if(status.isPresent()) {
			throw new LabelException("label already exist in user",400);
		}
	}
	
}
