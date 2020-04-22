package com.bridgelabz.fundoonotes.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import com.bridgelabz.fundoonotes.entity.Label;
import com.bridgelabz.fundoonotes.entity.Note;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long> {

	@Modifying

	@Transactional

	@Query(value = "delete from note where noteid =?1", nativeQuery = true)
	void deleteByNoteID(@PathVariable Long noteID);

	@Query(value = "select * from note where user_id=?1 and noteid=?2", nativeQuery = true)
	List<Label> getAllLabels(Long userID, Long noteID);

	@Query(value = "select * from note where user_id=?1 and pinned=true", nativeQuery = true)
	List<Note> findAllPinnedNotes(Long userID);

	@Query(value = "select * from note where user_id=?1 and trashed=true", nativeQuery = true)
	List<Note> getAllTrashedNotes(Long userID);

	@Query(value = "select * from note where user_id=?1 and archieved=true", nativeQuery = true)
	List<Note> getAllArchivedNotes(Long userID);

}
