package com.servicedao;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.ShelfSpace.ShelfSpace.Dto.BooksDetailsDto;
import com.ShelfSpace.ShelfSpace.Dto.StudentDetailsDto;
import com.ShelfSpace.ShelfSpace.entites.BooksDetails;
import com.ShelfSpace.ShelfSpace.entites.StudentDetails;

public interface ServiceDao {

	Optional<StudentDetails> saveStudentDetails(StudentDetails details);

	Optional<StudentDetails> updateStudentDetails(StudentDetails details, Long roll_no);

	Optional<StudentDetails> deleteStudentDetails(Long roll_no);

	Optional<BooksDetails> deleteStudentBooks(Long roll_no, Long book_id);

	Optional<StudentDetailsDto> getStudentDetailsDtoById(Long roll_no);

	Optional<StudentDetails> getStudentDetailsById(Long roll_no);

	Optional<Page<StudentDetails>> getAllStudent(Integer pageSize, Integer pageNumber);

	Optional<BooksDetails> getBookOwner(Long book_id);

	Optional<BooksDetails> getAllBooks();

	Optional<BooksDetails> addBooks(BooksDetailsDto booksDetailsDto, Long roll_no);

	void updateStudentNameAndReturnDate(Long roll_no, Long bookId, String name, String returnDate);
}
