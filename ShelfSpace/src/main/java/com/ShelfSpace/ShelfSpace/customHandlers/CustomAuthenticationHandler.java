package com.ShelfSpace.ShelfSpace.customHandlers;

import java.io.IOException;
import java.net.URLEncoder;

import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationHandler implements AuthenticationFailureHandler {
		
	  
		@Override
		public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
				org.springframework.security.core.AuthenticationException exception)
				throws IOException, ServletException {
			
			  String errorMessage = exception.getMessage();
		        System.out.println("ERROR MESSAGE: "+errorMessage);

		        response.sendRedirect("/Login?error=" + URLEncoder.encode(errorMessage, "UTF-8"));
			
		
	}
}
