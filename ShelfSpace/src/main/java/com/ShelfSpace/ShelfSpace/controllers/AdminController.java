// package com.ShelfSpace.ShelfSpace.controllers;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.validation.BindingResult;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;

// import com.ShelfSpace.ShelfSpace.Dto.StudentDetailsDto;
// import com.ShelfSpace.ShelfSpace.entites.StudentDetails;
// import com.ShelfSpace.ShelfSpace.repository.StudentDetailsRepository;
// import com.ShelfSpace.ShelfSpace.service.StudentDetailsService;

// import jakarta.validation.Valid;
// import lombok.extern.slf4j.Slf4j;

// @Controller
// @RequestMapping("/admin")
// @Slf4j
// public class AdminController {

// @Autowired
// // private StudentDetailsService studentDetailsService;

// @GetMapping("/getcreateuser")
// public String CreateUser(Model model) {
// log.warn("Entered in the /getcreateuser");
// StudentDetailsDto student = new StudentDetailsDto();
// model.addAttribute("studentsDetails", student);
// return "createuser";
// }

// @PostMapping("/processcreateuser")
// public String postMethodName(@Valid @ModelAttribute("studentsDetails")
// StudentDetailsDto studentDetails,
// BindingResult result, Model model) {
// if (result.hasErrors()) {
// return "createuser";
// }

// log.warn("We Have Arrived after the pojo class");

// // StudentDetails studentDetails2 = new
// StudentDetails(studentDetails.getName(),
// // studentDetails.getRoll_no(),
// // studentDetails.getEmail(), studentDetails.getPhoneNumber(),
// // studentDetails.getBooksDetails());
// log.warn("We got The Value of the StudentDetails class");

// // studentDetailsService.saveStudentDetails(studentDetails2);
// return "redirect:/";
// }

// }
