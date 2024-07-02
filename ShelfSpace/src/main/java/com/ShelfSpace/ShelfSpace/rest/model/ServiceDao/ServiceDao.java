package com.ShelfSpace.ShelfSpace.rest.model.ServiceDao;

import java.util.Optional;

import com.ShelfSpace.ShelfSpace.rest.model.BooksDetails;
import com.ShelfSpace.ShelfSpace.rest.model.StudentDetails;

public interface ServiceDao {

	Optional<StudentDetails> saveStudentDetails(StudentDetails details);

	Optional<StudentDetails> updateStudentDetails(StudentDetails details, Long roll_no);

	Optional<StudentDetails> deleteStudentDetails(Long roll_no);

	Optional<BooksDetails> deleteStudentBooks(Long book_id);

	Optional<StudentDetails> getStudentById(Long roll_no);

	Iterable<StudentDetails> getAllStudent();

	Optional<BooksDetails> getBookOwner(Long book_id);

	Iterable<BooksDetails> getAllBooks();
	
	Optional<BooksDetails>addBooks(BooksDetails booksDetails , Long roll_no);
}
