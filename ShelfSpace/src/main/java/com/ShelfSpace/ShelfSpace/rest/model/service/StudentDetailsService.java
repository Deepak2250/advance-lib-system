package com.ShelfSpace.ShelfSpace.rest.model.service;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ShelfSpace.ShelfSpace.rest.model.BooksDetails;
import com.ShelfSpace.ShelfSpace.rest.model.StudentDetails;
import com.ShelfSpace.ShelfSpace.rest.model.ServiceDao.ServiceDao;
import com.ShelfSpace.ShelfSpace.rest.model.repository.BookDetailsRepository;
import com.ShelfSpace.ShelfSpace.rest.model.repository.StudentDetailsRepository;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@Service
public class StudentDetailsService implements ServiceDao {

	private final StudentDetailsRepository detailsRepository;
	private final BookDetailsRepository bookDetailsRepository;

	@Autowired
	public StudentDetailsService(StudentDetailsRepository detailsRepository,
			BookDetailsRepository bookDetailsRepository) {
		this.detailsRepository = detailsRepository;
		this.bookDetailsRepository = bookDetailsRepository;
	}

	@Override
	public Optional<StudentDetails> saveStudentDetails(StudentDetails details) {
		for (BooksDetails booksDetails : details.getBooksDetails()) {
			booksDetails.setIssueDate(LocalDateTime.now());
			booksDetails.setUser(details);
		}

		return Optional.ofNullable(detailsRepository.save(details));
	}

	@Override
	public Optional<StudentDetails> updateStudentDetails(StudentDetails details, Long roll_no) {
		Optional<StudentDetails> id = detailsRepository.findById(roll_no);

		if (id.isEmpty()) {
			return Optional.empty();
		}

		StudentDetails existingStudent = id.get();
		existingStudent.setName(details.getName());
		existingStudent.setEmail(details.getEmail());
		existingStudent.setPhoneNumber(details.getPhoneNumber());
		
		details.getBooksDetails().forEach(booksDetails -> {
		        booksDetails.setIssueDate(LocalDateTime.now());
		        booksDetails.setUser(existingStudent);
		        existingStudent.getBooksDetails().add(booksDetails);
		 });

		 return Optional.of(detailsRepository.save(existingStudent));
	}

	
	@Override
	public Optional<StudentDetails> deleteStudentDetails(Long roll_no) {
		Optional<StudentDetails> optional = detailsRepository.findById(roll_no);
		if (optional.isPresent()) {
			StudentDetails details = optional.get();
			detailsRepository.delete(details);
            return Optional.of(details);
		}
		
		return Optional.empty();
	}
	

	@Override
	public Iterable<StudentDetails> getAllStudent() {
		Iterable<StudentDetails> stIterable =  detailsRepository.findAll();
		return stIterable;
	}

	@Override
	public Optional<BooksDetails>deleteStudentBooks(Long book_id) {
		Optional<BooksDetails> optional = bookDetailsRepository.findById(book_id);
		if (optional.isPresent()) {
			BooksDetails booksDetails = optional.get();
			bookDetailsRepository.delete(booksDetails);
			return Optional.of(booksDetails);
		}
	   
		return Optional.empty();
	}

	@Override
	public Optional<BooksDetails> getBookOwner(Long book_id) {
		return bookDetailsRepository.findById(book_id);
	}

	@Override
	public Iterable<BooksDetails> getAllBooks() {
		return bookDetailsRepository.findAll();
	}

	@Override
	public Optional<StudentDetails> getStudentById(Long roll_no) {
		return detailsRepository.findById(roll_no);
	}

	@Override
	public Optional<BooksDetails> addBooks(BooksDetails booksDetails ,  Long roll_no) {
		Optional<StudentDetails>studentObj = detailsRepository.findById(roll_no);
		if (studentObj.isPresent()) {
			if (booksDetails.getBookId() !=null) {
				return Optional.empty();
			}
			booksDetails.setIssueDate(LocalDateTime.now());
			booksDetails.setUser(studentObj.get());
			BooksDetails savedBook = bookDetailsRepository.save(booksDetails);
			return Optional.of(savedBook);
		}
		return Optional.empty();
	}
	
	
	
}
