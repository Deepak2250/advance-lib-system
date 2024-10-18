package com.ShelfSpace.ShelfSpace.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

import com.ShelfSpace.ShelfSpace.exception.BookAlreadyExist;
import com.ShelfSpace.ShelfSpace.exception.ResourceNotFoundException;
import com.ShelfSpace.ShelfSpace.exception.StudentNotFound;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceAccessException.class)
    @ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE) /// If the data is not successfully comeout from the External
                                                           /// Api
    public Map<String, String> handleResourceAccessException(ResourceAccessException exception) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Error", exception.getMessage());
        return errorMap;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException exception) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Error", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorMap);
    }

    @ExceptionHandler(StudentNotFound.class)
    public ResponseEntity<Map<String, String>> handleStudentNotFoundException(StudentNotFound exception) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Error", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorMap);
    }

    @ExceptionHandler(BookAlreadyExist.class)
    public ResponseEntity<Map<String, String>> handleBookAlreadyExistException(BookAlreadyExist exception) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Error", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorMap);
    }
}