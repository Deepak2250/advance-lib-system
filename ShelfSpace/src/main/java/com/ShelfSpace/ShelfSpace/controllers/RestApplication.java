package com.ShelfSpace.ShelfSpace.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

import com.ShelfSpace.ShelfSpace.Dto.BooksDetailsDto;
import com.ShelfSpace.ShelfSpace.Dto.StudentDetailsDto;
import com.ShelfSpace.ShelfSpace.Dto.StudentDetailsDtoMapper;
import com.ShelfSpace.ShelfSpace.entites.BooksDetails;
import com.ShelfSpace.ShelfSpace.entites.StudentDetails;
import com.ShelfSpace.ShelfSpace.model.StudentResponse;
import com.ShelfSpace.ShelfSpace.service.StudentDetailsService;

@RestController
@RequestMapping("/api/Student")
@CrossOrigin(origins = "http://localhost:3000")
public class RestApplication {

	@Autowired
	private StudentDetailsService detailsService;

	//// Get All The Students

	@GetMapping("/getAllStudents")
	public ResponseEntity<StudentResponse> getAllStudents(
			@RequestParam(value = "pageSize", defaultValue = "12", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber) {

		StudentResponse studentResponse = new StudentResponse();

		Optional<Page<StudentDetails>> studentDetailsIt = detailsService.getAllStudent(pageSize, pageNumber);

		if (studentDetailsIt.isPresent()) {
			List<StudentDetailsDto> detailsDtos = studentDetailsIt.get().getContent()
					.stream()
					.map(details -> StudentDetailsDtoMapper.toDto(details))
					.collect(Collectors.toList());

			studentResponse.setStudentDetails(detailsDtos);
			studentResponse.setHasMore(studentDetailsIt.get().hasNext()); // Check if there's a next page
		} else {
			studentResponse.setStudentDetails(new ArrayList<>());
			studentResponse.setHasMore(false);
		}
		return new ResponseEntity<>(studentResponse, HttpStatus.OK);
	}

	/// Adding The Students

	@PostMapping("/addStudent")
	public ResponseEntity<StudentDetails> addUser(@RequestBody StudentDetailsDto studentDetailsDto) {

		try {
			StudentDetails studentDetails = StudentDetailsDtoMapper.toEntity(studentDetailsDto);
			Optional<StudentDetails> optional = detailsService.saveStudentDetails(studentDetails);
			if (optional.isPresent()) {
				return new ResponseEntity<>(optional.get(), HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (ResourceAccessException ex) {
			throw new ResourceAccessException("Failed to access Exteranl Api : " + ex.getMessage());
		}
	}

	/// Updating The Students

	@PutMapping("/updateStudent/{roll_no}")
	public ResponseEntity<?> updateStudent(@RequestBody StudentDetails details, @PathVariable Long roll_no) {
		Optional<StudentDetails> updatedStudentOpt = detailsService.updateStudentDetails(details, roll_no);

		if (updatedStudentOpt.isPresent()) {
			return ResponseEntity.ok(updatedStudentOpt.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	/// Deleting the Student Details with Books

	@DeleteMapping("/deleteStudent/{roll_no}")
	public ResponseEntity<StudentDetails> deleteStudentDetails(@PathVariable Long roll_no) {
		Optional<StudentDetails> studentRollno = detailsService.deleteStudentDetails(roll_no);
		return studentRollno.map(student -> ResponseEntity.ok(student)).orElse(ResponseEntity.notFound().build());
	}

	/// Delete Only The Book Issued By the Student

	@DeleteMapping("/deleteBook")
	public ResponseEntity<BooksDetails> deleteBookDetails(@RequestParam Long roll_no, Long book_id) {
		Optional<BooksDetails> optional = detailsService.deleteStudentBooks(roll_no, book_id);
		return optional.map(book -> ResponseEntity.ok(book)).orElse(ResponseEntity.notFound().build());
	}

	/// Get the Owner of the of the book by passing book id

	@GetMapping("/getBookOwner/{book_id}")
	public ResponseEntity<BooksDetails> getBookOwenr(@PathVariable Long book_id) {
		Optional<BooksDetails> bookOptional = detailsService.getBookOwner(book_id);
		return bookOptional.map(bookOwner -> ResponseEntity.ok(bookOwner)).orElse(ResponseEntity.notFound().build());
	}

	/// get The Student by passing its roll_no

	@GetMapping("/getStudent/{roll_no}")
	public ResponseEntity<StudentDetailsDto> getStudentById(@PathVariable Long roll_no) {
		Optional<StudentDetailsDto> studentRollNo = detailsService.getStudentDetailsDtoById(roll_no);
		System.out.println("Your Dad Is here bitches " + studentRollNo.get());
		return studentRollNo.map(student -> ResponseEntity.ok(student)).orElse(ResponseEntity.notFound().build());
	}

	/// get All The books

	// @GetMapping("/getAllBooks")
	// public ResponseEntity<List<BooksDetails>> getAllBooks() {
	// // Iterable<BooksDetails> allBooks = detailsService.getAllBooks();
	// List<BooksDetails> allBooksList = new ArrayList<BooksDetails>();
	// allBooks.forEach(allBooksList::add);
	// if (allBooksList.isEmpty()) {
	// return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	// }
	// return new ResponseEntity<>(allBooksList, HttpStatus.OK);

	// }

	/// Add The books to the particular student

	@PostMapping("/addBook/{roll_no}")
	public ResponseEntity<?> addBooksToStudent(@RequestBody BooksDetailsDto booksDetailsDto,
			@PathVariable Long roll_no) {

		Optional<BooksDetails> studentBook = detailsService.addBooks(booksDetailsDto, roll_no);
		if (studentBook.isPresent()) {
			return ResponseEntity.ok(studentBook.get()); // Return the added book
		} else {
			return ResponseEntity.badRequest().build(); // Return a 400 Bad Request if the book could not be added
		}

	}

}
