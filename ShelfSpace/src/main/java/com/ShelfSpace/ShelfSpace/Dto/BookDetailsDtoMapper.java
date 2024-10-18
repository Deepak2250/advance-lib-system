package com.ShelfSpace.ShelfSpace.Dto;

import java.time.LocalDateTime;

import com.ShelfSpace.ShelfSpace.entites.BooksDetails;
import com.ShelfSpace.ShelfSpace.entites.StudentDetails;
import com.ShelfSpace.ShelfSpace.model.GoogleResponsePojo;
import com.ShelfSpace.ShelfSpace.service.ApiService;

public class BookDetailsDtoMapper {

    public static BooksDetails toEntity(BooksDetailsDto booksDetailsDto, StudentDetails studentDetails,
            ApiService apiService) {

        BooksDetails booksDetails = new BooksDetails();
        GoogleResponsePojo bookDetailsPojo = apiService.getBookDetails(booksDetails.getBookName());
        booksDetails.setBookId(booksDetailsDto.getBookId());
        booksDetails.setBookName(booksDetailsDto.getBookName());
        booksDetails.setReturnDate(booksDetailsDto.getReturnDate());
        booksDetails.setIssueDate(LocalDateTime.now());
        booksDetails.setUser(studentDetails);

        booksDetails.setAuthors(bookDetailsPojo.getAuthors());
        booksDetails.setDescription(bookDetailsPojo.getDescription());
        booksDetails.setImage(bookDetailsPojo.getImage());
        booksDetails.setPrice(bookDetailsPojo.getPrice());
        booksDetails.setPublishDate(bookDetailsPojo.getPublishedDate());
        booksDetails.setSubtitle(bookDetailsPojo.getSubtitle());
        booksDetails.setSynposis(bookDetailsPojo.getSynposis());
        booksDetails.setCurrencyCode(bookDetailsPojo.getCureencyCode());
        booksDetails.setBookPdf(bookDetailsPojo.getBookPdf());

        return booksDetails;
    }

    public static BooksDetailsDto toDto(BooksDetails booksDetails) {

        BooksDetailsDto booksDetailsDto = new BooksDetailsDto();
        booksDetailsDto.setBookId(booksDetails.getBookId());
        booksDetailsDto.setBookName(booksDetails.getBookName());
        booksDetailsDto.setIssueDateTime(booksDetails.getIssueDate());
        booksDetailsDto.setReturnDate(booksDetails.getReturnDate());
        booksDetailsDto.setAuthors(booksDetails.getAuthors());
        booksDetailsDto.setDescription(booksDetails.getDescription());
        booksDetailsDto.setImage(booksDetails.getImage());
        booksDetailsDto.setPrice(booksDetails.getPrice());
        booksDetailsDto.setPublishedDate(booksDetails.getPublishDate());
        booksDetailsDto.setSubtitle(booksDetails.getSubtitle());
        booksDetailsDto.setSynposis(booksDetails.getSynposis());
        booksDetailsDto.setCureencyCode(booksDetails.getCurrencyCode());
        booksDetailsDto.setBookPdf(booksDetails.getBookPdf());
        return booksDetailsDto;
    }
}