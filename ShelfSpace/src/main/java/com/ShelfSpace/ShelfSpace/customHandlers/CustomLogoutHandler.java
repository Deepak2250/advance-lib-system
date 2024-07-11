package com.ShelfSpace.ShelfSpace.customHandlers;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomLogoutHandler implements LogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		if (authentication != null && authentication.getPrincipal() != null) {

			CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
			String username = customUserDetails.getUsername();
			System.out.println("The " + username + " is Being logout");
			
			 // Invalidate session
	        request.getSession().invalidate();

	        // Clear authentication
	        SecurityContextHolder.clearContext();

	        // Optionally clear any cookies or other session-related data
	        // For example, if using cookies for authentication, clear them:
	        // CookieUtils.deleteCookie(request, response, "JSESSIONID");

	        // Redirect to home page or login page
			
			response.sendRedirect(request.getContextPath());

		}

	}

}
