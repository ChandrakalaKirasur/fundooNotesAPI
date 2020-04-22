package com.bridgelabz.fundoonotes.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.dto.EmailDTO;
import com.bridgelabz.fundoonotes.dto.LoginDTO;
import com.bridgelabz.fundoonotes.dto.ResetDTO;
import com.bridgelabz.fundoonotes.dto.UserDTO;
import com.bridgelabz.fundoonotes.entity.User;

@Service
public interface UserService {

	public boolean register(UserDTO userDto);

	boolean verifyEmail(String emailId);

	User login(LoginDTO loginDto);

	public boolean sendLinkForPassword(EmailDTO emailID);

	public boolean resetUserPassword(ResetDTO resetDto);

	public User getUser(String token);

	public User updateUser(String token, UserDTO userDto);

	public boolean deleteUser(Long userID, String token);
	
	public List<User> findAllUser();

}