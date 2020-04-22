package com.bridgelabz.fundoonotes.utility;

import java.io.Serializable;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class MailService implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public SimpleMailMessage mailService(String emailID, String emailBody, String emailSubject) {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(emailID);
		mail.setFrom("www.chandrakalakirasur67@gmail.com");
		mail.setSubject(emailSubject);
		mail.setText(emailBody);
		return mail;
	}
	private String receiverMail ;
	private String mailSubject;
	private String mailBody;
}
