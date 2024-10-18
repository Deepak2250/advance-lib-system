package com.ShelfSpace.ShelfSpace.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ShelfSpace.ShelfSpace.exception.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandlerForController {
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("isEmpty", true);
        model.addAttribute("errorMessage", ex.getMessage());
        return "bookissued"; // Redirect to the same Thymeleaf template
    }

}