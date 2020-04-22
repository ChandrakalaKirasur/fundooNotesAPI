package com.bridgelabz.fundoonotes.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.dto.LabelDTO;
import com.bridgelabz.fundoonotes.entity.Label;
import com.bridgelabz.fundoonotes.entity.Note;

@Service
public interface LabelService {

	Label createLabel(String token, LabelDTO labelDto);

	List<Label> getAllLabelsOfNote(String token, Long noteID);

	Label updateLabel(String token, Long labelID, String labelInformation);

	boolean deleteLabelFromUser(String token, Long labelID);

	List<Label> getAllLabels(String token);

	Label mapLabelToNote(String token, Long noteID, Long labelID);

	Label unMapLabelToNote(String token, Long noteID, Long labelID);

	List<Note> getAllNotesOfLabel(String token, Long labelID);

	Label getLabel(String token, String labelName);

	Label createLabelForNote(String token, String labelDto, long noteID);
}
