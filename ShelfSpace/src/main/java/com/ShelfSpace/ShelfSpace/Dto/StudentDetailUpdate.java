package com.ShelfSpace.ShelfSpace.Dto;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetailUpdate {

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 3, max = 20, message = "Name must be at least 3 characters long")
    private String name;

    @NotEmpty(message = "Email cannot be Empty")
    @Email(message = "Email should be valid", regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String email;

    @NotBlank(message = "Return date cannot be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private String returnDate;

    private Long roll_no;
    private Long bookId;
}
