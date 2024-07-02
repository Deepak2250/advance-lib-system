package com.ShelfSpace.ShelfSpace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ShelfSpace.ShelfSpace.model.User;
import com.ShelfSpace.ShelfSpace.model.UserInfo;
import com.ShelfSpace.ShelfSpace.service.UserService;

import jakarta.validation.Valid;

@Controller
public class ShelfBookController {
	
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/")
	public String homePage() {
		return "home";
	}
	
	@GetMapping("/LoginPage")
	public String loginPage() {
		return "login";
	}
	
	
	@GetMapping("/signup")
	public String registerPage(org.springframework.ui.Model model) {
		model.addAttribute(new UserInfo());
		return "signup";
	}
	
	@PostMapping("/processRegister")
	public String processRegister(@Valid @ModelAttribute("userInfo") UserInfo userInfo , BindingResult bindingResult , org.springframework.ui.Model model) {
		if (bindingResult.hasErrors()) {
			return"signup";
		}
	    User user = new User(userInfo.getName() , userInfo.getEmail() , userInfo.getPassword());
	    model.addAttribute("userInfo" , user);
	    userService.addUser(user);
	    return "success";
	}
	
	
}