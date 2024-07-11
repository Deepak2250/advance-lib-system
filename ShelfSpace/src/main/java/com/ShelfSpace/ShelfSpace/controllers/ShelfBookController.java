package com.ShelfSpace.ShelfSpace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ShelfSpace.ShelfSpace.model.User;
import com.ShelfSpace.ShelfSpace.model.UserInfo;
import com.ShelfSpace.ShelfSpace.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class ShelfBookController {

	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String homePage(Model model) {

		org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		boolean isAuthenticated = authentication != null && authentication.isAuthenticated() && !(authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken);
		
		boolean isAdmin = authentication
				.getAuthorities().stream()
				.anyMatch(auth -> auth.getAuthority() // The AnyMatch Stream will iterate over the authorities list and check if condition
					                                	// is True and even one is True then it will return it 
						.equals("ADMIN"));
		
		model.addAttribute("isAdmin", isAdmin);
		model.addAttribute("isAuthenticated", isAuthenticated);

		return "home";
	}
	

	@GetMapping("/Login")
	public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {

		 if (error != null) { 
	            model.addAttribute("error", "Email or Password is Incorrect"); 
	        }
	        return "login";
	}

	@GetMapping("/signup")
	public String registerPage(org.springframework.ui.Model model) {
		model.addAttribute(new UserInfo());
		return "signup";
	}

	@PostMapping("/processRegister")
	public String processRegister(@Valid @ModelAttribute("userInfo") UserInfo userInfo, BindingResult bindingResult,
			org.springframework.ui.Model model, HttpSession httpSession) {
		if (bindingResult.hasErrors()) {
			return "signup";
		}

		try {
			User user = new User(userInfo.getName(), userInfo.getEmail(), userInfo.getPassword());
			model.addAttribute("userInfo", user);
			User savedUser = userService.addUser(user);

			if (savedUser.getEmail().equals(userInfo.getEmail())) { // Checking if the user is added to the db then
				httpSession.setAttribute("userRegistered", true); // success either go for failure
				model.addAttribute("username", savedUser.getName());
				return "redirect:/"; // Go To Home Page if the user is Fully Registered Without any issue
			} else {
				return "signup"; // Else Go Back To the Same page
			}
		} catch (RuntimeException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "signup";
		}

	}

}