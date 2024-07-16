package com.ShelfSpace.ShelfSpace.customHandlers;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.ShelfSpace.ShelfSpace.entites.User;
import com.ShelfSpace.ShelfSpace.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationHandler implements AuthenticationFailureHandler {

	@Autowired
	private UserRepository userRepository;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			org.springframework.security.core.AuthenticationException exception) throws IOException, ServletException {

		String email = request.getParameter("email");
		boolean checkUser = verifyEmail(email);
		String errorMessage = exception.getMessage();

		if (!checkUser) {
			response.sendRedirect("/Login?error=email");
		} 
		else if (exception instanceof BadCredentialsException) {
			response.sendRedirect("/Login?error=password");
		} 
		
		System.out.println(errorMessage);

	}

	private boolean verifyEmail(String email) {

		Optional<User> userEmail = userRepository.findByEmail(email);
		return userEmail.isPresent();
	}
}
