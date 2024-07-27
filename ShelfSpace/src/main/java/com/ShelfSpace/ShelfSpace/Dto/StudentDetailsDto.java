package com.ShelfSpace.ShelfSpace.Dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDetailsDto {

    @NotEmpty(message = "Name cannot be Empty")
    @Size(min = 1, message = "Name must be at least 1 character long")
    private String name;

    @NotNull(message = "Roll number cannot be null")
    @Min(value = 1, message = "Roll number must be at least 1")
    private Long roll_no;

    @NotEmpty(message = "Email cannot be Empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Number cannot be Empty")
    private Long phoneNumber;

    @Valid
    private List<BooksDetailsDto> booksDetails = new ArrayList<>();

}
