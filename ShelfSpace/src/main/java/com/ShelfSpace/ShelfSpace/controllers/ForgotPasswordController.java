package com.ShelfSpace.ShelfSpace.controllers;

import java.time.Instant;
import java.util.Date;
import java.util.Random;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ShelfSpace.ShelfSpace.Dto.MailBody;
import com.ShelfSpace.ShelfSpace.entites.ForgotPassword;
import com.ShelfSpace.ShelfSpace.entites.User;
import com.ShelfSpace.ShelfSpace.exception.EmailNotFound;
import com.ShelfSpace.ShelfSpace.exception.OtpNotFoundExcpetion;
import com.ShelfSpace.ShelfSpace.repository.ForgotPasswordRepository;
import com.ShelfSpace.ShelfSpace.repository.UserRepository;
import com.ShelfSpace.ShelfSpace.service.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/forgot-password")
public class ForgotPasswordController {

	@Autowired
	private EmailService emailService;
	@Autowired
	private ForgotPasswordRepository forgotPasswordRepository;
	@Autowired
	private UserRepository userRepository;

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);

	@GetMapping("/email-Verification")
	public String emailVerification() {
		logger.info("Displaying emailVerification page.");
		return "emailVerification";
	}

	@PostMapping("/verify-email")
	public String verifyEmail(@RequestParam String email, Model model, HttpSession session) {
		logger.info("Received request to verify email: {}", email);
		try {
			User user = emailService.EmailChecker(email);
			if (user.getEmail() == null) {
				model.addAttribute("error", "No User Founded");
				logger.warn("No user found for email: {}", email);
				return "emailVerification";
			}	
            
			         ForgotPassword checkingForgotPassword = forgotPasswordRepository.findByUserObj(user);
			         if (checkingForgotPassword != null) {
					
					if (checkingForgotPassword.getOtpExpireDate().before(Date.from(Instant.now()))) {

						Long fId = checkingForgotPassword.getForgotpasswordId();
						System.out.println("The Password id is " + fId);

						User userid = checkingForgotPassword.getUser();
						// Remove the reference in the User entity
						userid.setForgotPassword(null);
						// Save the User entity to update the reference
						User savedUser = userRepository.save(userid);

						System.out.println("SAVED USER: " + savedUser.toString());

						emailService.deleteForgotPasswordById(fId);
						logger.warn("The Time is expired", checkingForgotPassword.getOtpExpireDate());
						throw new OtpNotFoundExcpetion("Otp Expired");
					}
					 else {
			        	 return "otpchecker";
			         }
			         }
			         else {
			   
					
					session.setAttribute("user", user);
					int otp = otpGenerator();
					MailBody mailBody = MailBody.builder().to(user.getEmail()).subject("Your Verification Code").text(
							"Dear User,\n\nThank you for registering with us. To complete your verification, please use the following OTP (One-Time Password):\n\n"
									+ otp
									+ "\n\nPlease do not share this OTP with anyone. If you did not request this, please ignore this email.\n\nBest regards,\nYour Company Name")
							.build();

					ForgotPassword forgotPassword = ForgotPassword.builder().otp(otp)
							.otpExpireDate(new Date(System.currentTimeMillis() + 3 * 60 * 1000)).user(user).build();

					System.out.println("OTP: " + forgotPassword.getOtp());
					System.out.println("Expiration Date: " + forgotPassword.getOtpExpireDate());

					emailService.sendSimpleMessage(mailBody);
					forgotPasswordRepository.save(forgotPassword);

					return "otpchecker";
			         }

		}

		catch (EmailNotFound e) {

			model.addAttribute("error", "No User Founded");
			logger.warn("Email not found: {}", email, e);
			return "emailVerification";
		}
		catch (DuplicateKeyException e) {
			model.addAttribute("error", "An OTP already exists for this user");
			logger.warn("Duplicate key exception for email: {}", email, e);
			return "emailVerification";
		}

		catch (Exception e) {
			model.addAttribute("error", "An error occurred while processing your request");
			logger.error("An unexpected error occurred for email: {}", email, e);
			return "emailVerification";
		}
}



	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam String otp, Model model, HttpSession session) {
		logger.info("The Entered in the Verify otp section");
		User user = (User) session.getAttribute("user");
		if (user == null) {
			logger.warn("No User Foud");
			model.addAttribute("error", "No User Found");
		}
		try {
			int otpInt;
			try {
				otpInt = Integer.parseInt(otp);
			} catch (NumberFormatException e) {
				logger.warn("Invalid OTP format: {}", otp);
				model.addAttribute("error", "Invalid OTP format");
				return "otpchecker";
			}

			ForgotPassword forgotPassword = emailService.optChecker(otpInt, user);

			if (forgotPassword == null) {
				logger.warn("ForgotPassword not found for OTP {} and email {}", otp, user.getEmail());
				model.addAttribute("error", "OTP not found");
				return "otpchecker";
			}

			if (forgotPassword.getOtpExpireDate().before(Date.from(Instant.now()))) {

				Long fId = forgotPassword.getForgotpasswordId();

				User userid = forgotPassword.getUser();
				// Remove the reference in the User entity
				userid.setForgotPassword(null);
				// Save the User entity to update the reference
			     userRepository.save(userid);

				emailService.deleteForgotPasswordById(fId);
				logger.warn("The Time is expired", forgotPassword.getOtpExpireDate());
				throw new OtpNotFoundExcpetion("Otp Expired");
			}
			logger.warn("The user is founded", user);
			return "resetpassword";

		} 
		catch (DuplicateKeyException e) {
			model.addAttribute("error", "Wait For 3 Minutes");
			logger.warn("Duplicate key exception for email: {}", e);
			return "otpchecker";
		}
		
		catch (OtpNotFoundExcpetion e) {
			logger.error("The error occured", e);
			model.addAttribute("error", "Otp is Invalid");
			return "otpchecker";
		} catch (Exception e) {
			logger.error("The error occured", e);
			model.addAttribute("error", "Otp is Invalid");
			return "otpchecker";
		}
	}

	@PostMapping("/resetPassword")
	public String resetPassword(HttpSession session) {
		User user = (User) session.getAttribute("user");
		userRepository.resetPassword(null, null)
	}
	
	private Integer otpGenerator() {
		Random random = new Random();
		return random.nextInt(900000) + 100000;
	}

}
