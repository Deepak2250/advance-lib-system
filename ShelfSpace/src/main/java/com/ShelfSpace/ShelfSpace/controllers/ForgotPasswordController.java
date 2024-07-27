package com.ShelfSpace.ShelfSpace.controllers;

import java.time.Instant;
import java.util.Date;
import java.util.Random;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ShelfSpace.ShelfSpace.Dto.MailBody;
import com.ShelfSpace.ShelfSpace.entites.ForgotPassword;
import com.ShelfSpace.ShelfSpace.entites.User;
import com.ShelfSpace.ShelfSpace.exception.EmailNotFound;
import com.ShelfSpace.ShelfSpace.exception.OtpNotFoundExcpetion;
import com.ShelfSpace.ShelfSpace.exception.PasswordNotCorrectException;
import com.ShelfSpace.ShelfSpace.model.ResetPassword;
import com.ShelfSpace.ShelfSpace.repository.ForgotPasswordRepository;
import com.ShelfSpace.ShelfSpace.repository.UserRepository;
import com.ShelfSpace.ShelfSpace.service.EmailService;
import com.ShelfSpace.ShelfSpace.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/forgot-password")
public class ForgotPasswordController {

	@Autowired
	private EmailService emailService;
	@Autowired
	private ForgotPasswordRepository forgotPasswordRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;

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
			model.addAttribute("resetPassword", new ResetPassword());
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

	@PostMapping("/reset-password")
	public String resetPassword( @Valid @ModelAttribute("resetPassword") ResetPassword resetPassword  , BindingResult result, HttpSession session , Model model) {
		
		logger.warn("Entered to the reset Password successfully ");
		if (result.hasErrors()) {
			logger.warn("The validation error occurs ");
			return "resetpassword";
		}
		
		try {
		User user = (User) session.getAttribute("user");
		if (userService.resetPassword(resetPassword, user)) {
			MailBody mailBody = MailBody.builder()
				    .to(user.getEmail())
				    .subject("Password Changed Successfully")
				    .text("Dear " + user.getName() + ",\n\n"
				          + "We wanted to let you know that your password was changed successfully.\n\n"
				          + "If you did not make this change, please contact our support team immediately.\n\n"
				          + "Here are some tips to keep your account secure:\n"
				          + "- Do not share your password with anyone.\n"
				          + "- Use a unique password for every account.\n"
				          + "- Change your password regularly.\n\n"
				          + "If you have any questions, feel free to reach out to our support team.\n\n"
				          + "Best regards,\n"
				          + "DanixVerse\n"
				          + "Support Team\n"
				          + "danixverset@gmail.com")
				    .build();

			emailService.sendSimpleMessage(mailBody);
			ForgotPassword forgotPassword = forgotPasswordRepository.findByUserObj(user); // Deleting the user and the otp token after the 
			                                                                              // User Password is been Deleted;
			Long forgotPasswordId = forgotPassword.getForgotpasswordId();
			User userObj = forgotPassword.getUser();
			userObj.setForgotPassword(null);
			forgotPasswordRepository.deleteById(forgotPasswordId);
			logger.info("The User otp token is been Removed");
			
			model.addAttribute("userName", user.getName());
			logger.warn("The password has been changed successfully for user: {}", user.getEmail());
            
			return "success";
		}
		else {
			logger.error("The error occured");
			model.addAttribute("error","Both Passoword Are Different");
			logger.error("The password hasnot been changed"  , user.getPassword());
			return "resetpassword";
		}
		}
		catch (NullPointerException e) {
			logger.error("Null pointer exception" , e);
			return "emailVerification";
		}
		catch (PasswordNotCorrectException e) {
			logger.error("PasswordNotCorrectException " , e);
			model.addAttribute("error","Both Passoword Are Different");
			return "resetpassword";
		}
		
	}
	
	private Integer otpGenerator() {
		Random random = new Random();
		return random.nextInt(900000) + 100000;
	}

}
