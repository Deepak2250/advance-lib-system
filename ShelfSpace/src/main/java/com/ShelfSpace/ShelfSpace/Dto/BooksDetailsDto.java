package com.ShelfSpace.ShelfSpace.Dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BooksDetailsDto {

    @NotNull(message = "No the Book id cant be null")
    private Long bookId;

    @NotNull(message = "No the Bookname cant be null")
    private String bookName;

    @NotNull(message = "The Return date Cant be null")
    private LocalDateTime returnDate;

    private LocalDateTime issueDateTime;
    private String subtitle;
    private String authors;
    private String publishedDate;
    private String description;

    private String image;

    private String price;
    private String cureencyCode;

    private String synposis;

    private String bookPdf;

}