package com.ShelfSpace.ShelfSpace.Dto;

import java.util.List;

import com.ShelfSpace.ShelfSpace.entites.BooksDetails;
import com.ShelfSpace.ShelfSpace.entites.StudentDetails;

public class StudentDetailsDtoMapper {

    public static StudentDetails toEntity(StudentDetailsDto studentDetailsDto) {

        StudentDetails studentDetails = new StudentDetails();
        studentDetails.setName(studentDetailsDto.getName());
        studentDetails.setEmail(studentDetailsDto.getEmail());
        studentDetails.setPhoneNumber(studentDetailsDto.getPhoneNumber());
        studentDetails.setRoll_no(studentDetailsDto.getRoll_no());

        List<BooksDetails> booksDetailsList = studentDetailsDto.getBooksDetails().stream().map(booksDetailsDto -> {
            BooksDetails booksDetails = new BooksDetails();
            booksDetails.setBookId(booksDetailsDto.getBookId());
            booksDetails.setBookName(booksDetailsDto.getBookName());
            booksDetails.setReturnDate(booksDetailsDto.getReturnDate());
            return booksDetails;

        }).toList();

        studentDetails.setBooksDetails(booksDetailsList);
        return studentDetails;
    }

    public static StudentDetailsDto toDto(StudentDetails studentDetails) {

        StudentDetailsDto studentDetailsDto = new StudentDetailsDto();
        studentDetailsDto.setName(studentDetails.getName());
        studentDetailsDto.setEmail(studentDetails.getEmail());
        studentDetailsDto.setRoll_no(studentDetails.getRoll_no());

        List<BooksDetailsDto> booksDetailDtoList = studentDetails.getBooksDetails().stream().map(booksDetails -> {
            BooksDetailsDto booksDetailsDto = new BooksDetailsDto();
            booksDetailsDto.setBookId(booksDetails.getBookId());
            booksDetailsDto.setBookName(booksDetails.getBookName());
            booksDetailsDto.setReturnDate(booksDetails.getReturnDate());
            booksDetailsDto.setAuthors(booksDetails.getAuthors());
            booksDetailsDto.setBookPdf(booksDetails.getBookPdf());
            booksDetailsDto.setCureencyCode(booksDetails.getCurrencyCode());
            booksDetailsDto.setDescription(booksDetails.getDescription());
            booksDetailsDto.setImage(booksDetails.getImage());
            booksDetailsDto.setPrice(booksDetails.getPrice());
            booksDetailsDto.setSubtitle(booksDetails.getSubtitle());
            booksDetailsDto.setSynposis(booksDetails.getSynposis());

            return booksDetailsDto;
        }).toList();

        studentDetailsDto.setBooksDetails(booksDetailDtoList);
        return studentDetailsDto;
    }
}