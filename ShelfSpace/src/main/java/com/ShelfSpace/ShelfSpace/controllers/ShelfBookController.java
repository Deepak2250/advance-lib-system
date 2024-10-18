package com.ShelfSpace.ShelfSpace.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ShelfSpace.ShelfSpace.Dto.StudentDetailsDto;
import com.ShelfSpace.ShelfSpace.entites.BooksDetails;
import com.ShelfSpace.ShelfSpace.entites.StudentDetails;
import com.ShelfSpace.ShelfSpace.entites.User;
import com.ShelfSpace.ShelfSpace.model.UserInfo;
import com.ShelfSpace.ShelfSpace.service.StudentDetailsService;
import com.ShelfSpace.ShelfSpace.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ShelfBookController {

	@Autowired
	private UserService userService;
	@Autowired
	private StudentDetailsService studentDetailsService;

	@GetMapping("/")
	public String homePage(Model model) {
		checkIsAdmin(model);
		return "home";
	}

	@GetMapping("/Login")
	public String loginPage(@RequestParam(value = "error", required = false) String error, Model model,
			HttpSession session) {

		log.warn("We entered in the /Login Enpoint ");
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

		if (error != null) {
			if (error.equals("email")) {
				model.addAttribute("error", "You Are Not Registered Yet");
				log.error("The error is Occured");
			} else if (error.equals("password")) {
				model.addAttribute("error", "Password is Incorrect");
				log.error("The error is Occured");
			}

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

	@GetMapping("/viewmore")
	public String getViewMore(@RequestParam("roll_no") Long roll_no, Model model) {
		Optional<StudentDetailsDto> studentDetails = studentDetailsService.getStudentDetailsDtoById(roll_no);
		studentDetails.ifPresent(studentDetail -> {
			model.addAttribute("studentDetail", studentDetail);
		});
		checkIsAdmin(model);
		return "viewmore";
	}

	private void checkIsAdmin(Model model) {
		org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		boolean isAuthenticated = authentication != null && authentication.isAuthenticated()
				&& !(authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken);

		boolean isAdmin = false;
		if (authentication != null) {
			isAdmin = authentication.getAuthorities().stream()
					.anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
		}

		model.addAttribute("isAdmin", isAdmin);
		model.addAttribute("isAuthenticated", isAuthenticated);
	}

	@GetMapping("/issuedbook")
	public String getIssuedBook(Model model) {
		Optional<Page<StudentDetails>> studentBooks = studentDetailsService.getAllStudent(12, 1);
		if (!studentBooks.isPresent() || studentBooks.get().isEmpty()) {
			model.addAttribute("isEmpty", true);
			return "bookissued"; // Render the view with the empty state
		}
		List<StudentDetails> students = studentBooks.map(Page::getContent).orElse(Collections.emptyList());
		String gridClass = getGridClass(students.size());

		// Add attributes to the model
		model.addAttribute("studentBooks", students);
		model.addAttribute("gridClass", gridClass);
		model.addAttribute("isEmpty", false);

		return "bookissued"; // Render the Thymeleaf view
	}

	private String getGridClass(int dataLength) {
		if (dataLength == 1) {
			return "grid grid-cols-2 grid-rows-1 sm:grid-cols-1 md:grid-cols-1 lg:grid-cols-1 sm:grid-rows-1 md:grid-rows-1 lg:grid-rows-1";
		} else if (dataLength <= 2) {
			return "grid grid-cols-2 grid-rows-1 sm:grid-cols-2 md:grid-cols-2 lg:grid-cols-4 sm:grid-rows-1 md:grid-rows-1 lg:grid-rows-1";
		} else if (dataLength > 2 && dataLength <= 4) {
			return "grid grid-cols-2 grid-rows-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 sm:grid-rows-2 md:grid-rows-2 lg:grid-rows-1";
		} else if (dataLength > 4 && dataLength <= 8) {
			return "grid grid-cols-2 grid-rows-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 sm:grid-rows-4 md:grid-rows-3 lg:grid-rows-2";
		} else if (dataLength > 8 && dataLength <= 12) {
			return "grid grid-cols-2 grid-rows-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 sm:grid-rows-6 md:grid-rows-4 lg:grid-rows-3";
		}
		return "";
	}

}