package com.ShelfSpace.ShelfSpace.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ShelfSpace.ShelfSpace.Dto.StudentDetailUpdate;
import com.ShelfSpace.ShelfSpace.Dto.StudentDetailsDto;
import com.ShelfSpace.ShelfSpace.Dto.StudentDetailsDtoMapper;
import com.ShelfSpace.ShelfSpace.entites.StudentDetails;
import com.ShelfSpace.ShelfSpace.exception.EmailAlreadyExistsException;
import com.ShelfSpace.ShelfSpace.exception.ResourceNotFoundException;
import com.ShelfSpace.ShelfSpace.exception.StudentNotFound;
import com.ShelfSpace.ShelfSpace.service.StudentDetailsService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private StudentDetailsService studentDetailsService;

    @GetMapping("/getcreateuser")
    public String createUser(Model model) {
        StudentDetailsDto student = new StudentDetailsDto();
        model.addAttribute("studentsDetails", student);
        return "createuser";
    }

    @PostMapping("/processcreateuser")
    public String postMethodName(@Valid @ModelAttribute("studentsDetails") StudentDetailsDto studentDetails,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "createuser";
        }

        StudentDetails studentDetails2 = StudentDetailsDtoMapper.toEntity(studentDetails);
        studentDetailsService.saveStudentDetails(studentDetails2);
        return "redirect:/";
    }

    @GetMapping("/studentallbooks")
    public String studentAllBooks(@RequestParam Long roll_no, Model model) {
        Optional<StudentDetailsDto> studentALlBooks = studentDetailsService.getStudentDetailsDtoById(roll_no);
        model.addAttribute("studentAllBooks", studentALlBooks.get());
        return "allbooks";
    }

    @PostMapping("/deletestudentbook")
    public String deleteStudentBook(@RequestParam Long roll_no, Long book_id, Model model) {
        StudentDetails studentDetails = studentDetailsService.getStudentDetailsById(roll_no)
                .orElseThrow(() -> {
                    return new StudentNotFound("No Student Found");
                });

        model.addAttribute("studentAllBooks", studentDetails);

        if (studentDetails.getBooksDetails().size() == 1) {
            studentDetailsService.deleteStudentDetails(roll_no);
            return "redirect:/";
        } else {
            studentDetailsService.deleteStudentBooks(roll_no, book_id);
            return "allbooks";
        }

    }

    @PostMapping("/deletestudent")
    public String deleteStudent(@RequestParam Long roll_no, Model model) {
        Optional<StudentDetails> studentDetails = studentDetailsService.getStudentDetailsById(roll_no);
        if (studentDetails.isPresent()) {
            model.addAttribute("deletedStudent", studentDetails.get().getName());
        }
        studentDetailsService.deleteStudentDetails(roll_no); // Delete the student from here
        return "deletedstudent";
    }

    @GetMapping("/edit")
    public String getUpdateStudent(@RequestParam Long roll_no, @RequestParam Long bookId, Model model) {
        log.info("Received roll_no: {}, bookId: {}", roll_no, bookId);
        StudentDetailUpdate studentDetailUpdate = new StudentDetailUpdate();
        model.addAttribute("roll_no", roll_no);
        model.addAttribute("bookId", bookId);
        model.addAttribute("studentDetails", studentDetailUpdate);
        return "updatestudent";
    }

    @PostMapping("/updatestudent")
    public String updateStudent(@Valid @ModelAttribute("studentDetails") StudentDetailUpdate studentDetailUpdate,
            BindingResult result, Model model) {

        log.info("Updating student with roll_no: {}, bookId: {}", studentDetailUpdate.getRoll_no(),
                studentDetailUpdate.getBookId());

        if (result.hasErrors()) {
            return "updatestudent";
        }

        try {
            studentDetailsService.updateStudentNameAndReturnDate(studentDetailUpdate.getRoll_no(),
                    studentDetailUpdate.getBookId(), studentDetailUpdate.getName(), studentDetailUpdate.getEmail(),
                    studentDetailUpdate.getReturnDate());

            return "redirect:/viewmore?roll_no=" + studentDetailUpdate.getRoll_no();

        }

        catch (EmailAlreadyExistsException e) {
            model.addAttribute("emailError", e.getMessage());
            return "updateStudent";

        }

        catch (StudentNotFound | ResourceNotFoundException e) {
            return "rediect:/updatestudent";
        }
    }
}