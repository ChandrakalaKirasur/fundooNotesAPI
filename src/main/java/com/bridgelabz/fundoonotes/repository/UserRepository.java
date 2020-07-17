package com.bridgelabz.fundoonotes.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.entity.Note;
import com.bridgelabz.fundoonotes.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	public Optional<User> findByUserID(Long userID);

	public boolean deleteByUserID(Long userID);

	public Optional<User> findByEmailId(String emailID);

	public boolean existsByFirstName(String userName);

	public Optional<User> findByFirstName(String userName);

	default List<Note> getAllTrashedNotes(Long userID) {
		Optional<User> user = findByUserID(userID);
		return user.get().getNotes().stream().filter(p -> p.isTrashed() == true).collect(Collectors.toList());
	}

	default List<Note> getAllArchivedNotes(Long userID) {
		Optional<User> user = findByUserID(userID);
		return user.get().getNotes().stream().filter(p -> p.isArchieved() == true && p.isTrashed()==false).collect(Collectors.toList());
	}

	default List<Note> findAllPinnedNotes(Long userID) {
		Optional<User> user = findByUserID(userID);
		return user.get().getNotes().stream().filter(p -> p.isPinned() == true).collect(Collectors.toList());
	}

	default List<Note> findAllReminderNotes(Long userID){
		Optional<User> user = findByUserID(userID);
		return user.get().getNotes().stream().filter(p -> p.getReminderDate() != null).collect(Collectors.toList());
	}

	public default int[] retrieveAllData() {
		 return new int[] {1,2,4,8};
	}

}
