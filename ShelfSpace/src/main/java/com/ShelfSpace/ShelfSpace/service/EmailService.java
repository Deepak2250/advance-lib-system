package com.ShelfSpace.ShelfSpace.service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ShelfSpace.ShelfSpace.Dto.MailBody;
import com.ShelfSpace.ShelfSpace.entites.ForgotPassword;
import com.ShelfSpace.ShelfSpace.entites.User;
import com.ShelfSpace.ShelfSpace.exception.EmailNotFound;
import com.ShelfSpace.ShelfSpace.exception.OtpAlreadyPresent;
import com.ShelfSpace.ShelfSpace.exception.OtpNotFoundExcpetion;
import com.ShelfSpace.ShelfSpace.repository.ForgotPasswordRepository;
import com.ShelfSpace.ShelfSpace.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ForgotPasswordRepository forgotPasswordRepository;
	
	 @Value("${spring.mail.username}")
	    private String fromEmail; // Injecting the 'spring.mail.username' property

	
	public void sendSimpleMessage(MailBody mailBody) { // MailBody is a Record Who Takes The Data of Our Mail Body and don't need any getter
		                                               // and Setters For it bcz it already have it and its really good of u want any 
		                                               // Immutable Object and it come in java 14 
		
		SimpleMailMessage mailMessage = new SimpleMailMessage(); //A simple and straightforward class for creating basic email messages in Spring applications.
		mailMessage.setTo(mailBody.to());
		mailMessage.setFrom(fromEmail);
		mailMessage.setSubject(mailBody.subject());
		mailMessage.setText(mailBody.text());
		javaMailSender.send(mailMessage); // this will send the message towards the email
	}
	
	// Check for the Email 
	
	public User EmailChecker(String email) {
	Optional<User> userEmail = userRepository.findByEmail(email);
	
	if (!userEmail.isPresent()) {
		throw new EmailNotFound("No Email Founded By this " +email);
	}
	else {
		return userEmail.get();
	}
	
	}
	
	//Check for the email
	
	public ForgotPassword optChecker(int otp , User user) {
	Optional<ForgotPassword> fpOtp = forgotPasswordRepository.findByOtpAndUser(otp, user);
	if (!fpOtp.isPresent()) {
		throw new OtpNotFoundExcpetion("The Otp Not Found or Being Expired");
	}
	else {
		return fpOtp.get();
	}
	}
	
	@Transactional
	public void deleteForgotPasswordById(Long id) {
	    forgotPasswordRepository.deleteById(id);

	}
}
