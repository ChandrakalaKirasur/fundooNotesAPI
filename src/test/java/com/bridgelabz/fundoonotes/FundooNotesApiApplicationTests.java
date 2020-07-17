package com.bridgelabz.fundoonotes;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bridgelabz.fundoonotes.dto.LoginDTO;
import com.bridgelabz.fundoonotes.dto.UserDTO;
import com.bridgelabz.fundoonotes.entity.User;
import com.bridgelabz.fundoonotes.repository.UserRepository;
import com.bridgelabz.fundoonotes.service.impl.UserServiceImpl;

@SpringBootTest
class FundooNotesApiApplicationTests {

	@Mock
	private UserRepository repo;
	
	@InjectMocks
	UserServiceImpl userImpl;

	@Mock
	private BCryptPasswordEncoder encoder;
	
	@Test
	public void loginTestCase() {
		Long id = (long) 2;
		User newUser = new User(id, "ChandrakalaVK", "chandrakala", "chandra123","8147267142", "chandrakalakirasur@gmail.com", null, false, null, null, null);
		newUser.setPassword(encoder.encode(newUser.getPassword()));
		when(repo.findAll()).thenReturn(Stream.of(new User(id, "ChandrakalaVK", "chandrakala", newUser.getPassword(), "8147267142", "chandrakalakirasur@gmail.com", null, false, null, null, null)).collect(Collectors.toList()));
		assertEquals("chandrakalakirasur@gmail.com",userImpl.findAllUser().stream().filter(l->l.getEmailId().equals("chandrakalakirasur@gmail.com")).findFirst().get().getEmailId());
		assertEquals(3, 3);
	}
	@Test
	public void testFindTheGreatestFromAllData() {
		when(repo.retrieveAllData()).thenReturn(new int[] { 24, 15, 3 });
		assertEquals(24, userImpl.findTheGreatestFromAllData());
	}
	
	@Test
	public void testLogin() {
		Long id = (long) 2;
		LoginDTO loginDto= new LoginDTO();
		loginDto.setEmailId("chandrakalakirasur@gmail.com");
		loginDto.setPassword("chandra123");
		User newUser = new User(id, "ChandrakalaVK", "chandrakala", "chandra123","8147267142", "chandrakalakirasur@gmail.com", null, false, null, null, null);
		Mockito.when(encoder.matches("chandra123", newUser.getPassword())).thenReturn(true);
		Mockito.when(repo.findByEmailId("chandrakalakirasur@gmail.com")).thenReturn(Optional.of(newUser));
		assertEquals(newUser.getLastName(), userImpl.login(loginDto).getLastName());
	}
	
	@Test
	public void register() {
		Long id = (long) 2;
		UserDTO userDto=new UserDTO();
		User newUser = new User(id, "ChandrakalaVK", "chandrakala", "chandra123","8147267142", "www.chandrakalakirasur@gmail.com", null, false, null, null, null);
		userDto.setFirstName(newUser.getFirstName());
		userDto.setLastName(newUser.getLastName());
		userDto.setEmailId(newUser.getEmailId());
		userDto.setPhoneNumber(newUser.getPhoneNum());
		userDto.setPassword(newUser.getPassword());
		doReturn("chandrakala123").when(encoder).encode(newUser.getPassword());
		when(repo.existsByFirstName(newUser.getFirstName())).thenReturn(false);
		when(repo.save(newUser)).thenReturn(newUser);
		assertEquals(true, userImpl.register(userDto));
	}
}
