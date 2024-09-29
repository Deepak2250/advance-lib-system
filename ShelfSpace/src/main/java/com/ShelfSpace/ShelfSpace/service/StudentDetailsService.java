package com.ShelfSpace.ShelfSpace.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ShelfSpace.ShelfSpace.Dto.BookDetailsDtoMapper;
import com.ShelfSpace.ShelfSpace.Dto.BooksDetailsDto;
import com.ShelfSpace.ShelfSpace.Dto.StudentDetailsDto;
import com.ShelfSpace.ShelfSpace.Dto.StudentDetailsDtoMapper;
import com.ShelfSpace.ShelfSpace.entites.BooksDetails;
import com.ShelfSpace.ShelfSpace.entites.StudentDetails;
import com.ShelfSpace.ShelfSpace.exception.BookAlreadyExist;
import com.ShelfSpace.ShelfSpace.exception.ResourceNotFoundException;
import com.ShelfSpace.ShelfSpace.exception.StudentNotFound;
import com.ShelfSpace.ShelfSpace.model.GoogleResponsePojo;
import com.ShelfSpace.ShelfSpace.repository.BookDetailsRepository;
import com.ShelfSpace.ShelfSpace.repository.StudentDetailsRepository;
import com.servicedao.ServiceDao;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StudentDetailsService implements ServiceDao {

	private final StudentDetailsRepository detailsRepository;
	private final BookDetailsRepository bookDetailsRepository;
	private final ApiService apiService;

	// @Autowired
	// private BookDetailsService service;

	public StudentDetailsService(StudentDetailsRepository detailsRepository,
			BookDetailsRepository bookDetailsRepository, ApiService apiService) {
		log.warn("We have entered in the StudentDetails Service class");
		this.detailsRepository = detailsRepository;
		this.bookDetailsRepository = bookDetailsRepository;
		this.apiService = apiService;
	}

	@Override
	public Optional<StudentDetails> saveStudentDetails(StudentDetails userDetails) {

		Optional<StudentDetails> sOptional = detailsRepository.findByRollNo(userDetails.getRoll_no());
		Optional<BooksDetails> bOptional = bookDetailsRepository
				.findByBookId(userDetails.getBooksDetails().get(0).getBookId());
		if (sOptional.isPresent() || bOptional.isPresent()) {
			return Optional.empty();
		}

		if (userDetails.getBooksDetails() != null && !userDetails.getBooksDetails().isEmpty()) {
			GoogleResponsePojo bookDetails = apiService
					.getBookDetails(userDetails.getBooksDetails().get(0).getBookName());

			userDetails.getBooksDetails().forEach(booksDetails -> {
				booksDetails.setIssueDate(LocalDateTime.now());
				booksDetails.setAuthors(bookDetails.getAuthors());
				booksDetails.setCurrencyCode(bookDetails.getCureencyCode());
				booksDetails.setDescription(bookDetails.getDescription());
				booksDetails.setImage(bookDetails.getImage());
				booksDetails.setPrice(bookDetails.getPrice());
				booksDetails.setSubtitle(bookDetails.getSubtitle());
				booksDetails.setSynposis(bookDetails.getSynposis());
				booksDetails.setBookPdf(bookDetails.getBookPdf());
				booksDetails.setUser(userDetails);
			});

		}

		else {
			log.warn("No books details available for user: {}", userDetails.getName());
		}

		return Optional.of(detailsRepository.save(userDetails));
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
	public Optional<Page<StudentDetails>> getAllStudent(Integer pageSize, Integer pageNumber) {

		org.springframework.data.domain.Pageable pageable = PageRequest.of(pageNumber, pageSize);

		Page<StudentDetails> studentDetailsList = detailsRepository.findAll(pageable);
		if (studentDetailsList.isEmpty()) {
			throw new ResourceNotFoundException("No Resource founded");
		}
		return Optional.of(studentDetailsList);
	}

	@Override
	public Optional<BooksDetails> deleteStudentBooks(Long roll_no, Long book_id) {
		StudentDetails studentDetails = getStudentDetailsById(roll_no)
				.orElseThrow(() -> new StudentNotFound("No Student Found"));

		BooksDetails booksDetails = bookDetailsRepository.findById(book_id) // Find the book to be deleted
				.orElseThrow(() -> new ResourceNotFoundException("No founded"));

		studentDetails.getBooksDetails().remove(booksDetails); // remove the book from the list of books
		booksDetails.setUser(null); // set the particular book student or user to null so that the connection of
									// books breaks with the user and we can delete it without getting
									// DataIntegrityViolationException
		detailsRepository.save(studentDetails); // Now Save the studentDetails after removing the book from it
		bookDetailsRepository.delete(booksDetails); // Now delete the book with the help of bookDetailsRepository
													// without doing anything with the user
		return Optional.ofNullable(booksDetails);
	}

	@Override
	public Optional<BooksDetails> getBookOwner(Long book_id) {
		return bookDetailsRepository.findById(book_id);
	}

	// @Override
	// public Optional<BooksDetails> getAllBooks() {
	// List<BooksDetails> bookDetails = bookDetailsRepository.findAll();
	// }

	@Override
	public Optional<StudentDetailsDto> getStudentDetailsDtoById(Long roll_no) {
		Optional<StudentDetails> studentDetails = detailsRepository.findById(roll_no);
		if (studentDetails.isPresent()) {
			return Optional.of(StudentDetailsDtoMapper.toDto(studentDetails.get()));
		}
		return Optional.empty();
	}

	@Override
	public Optional<BooksDetails> addBooks(BooksDetailsDto booksDetailsDto, Long roll_no) {

		Optional<StudentDetails> studentObj = detailsRepository.findById(roll_no);
		Optional<BooksDetails> existingBook = bookDetailsRepository.findByBookId(booksDetailsDto.getBookId());

		if (!studentObj.isPresent()) {
			throw new StudentNotFound("No Student Has been Founded with this roll_no");
		}

		if (existingBook.isPresent()) {
			throw new BookAlreadyExist("This Book is Already Present");
		}
		BooksDetails booksDetails = BookDetailsDtoMapper.toEntity(booksDetailsDto, studentObj.get(),
				apiService);
		BooksDetails savedBook = bookDetailsRepository.save(booksDetails);
		return Optional.of(savedBook);
	}

	@Override
	public Optional<BooksDetails> getAllBooks() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getAllBooks'");
	}

	@Override
	public Optional<StudentDetails> getStudentDetailsById(Long roll_no) {
		Optional<StudentDetails> studentDetails = detailsRepository.findById(roll_no);
		if (studentDetails.isPresent()) {
			return Optional.of(studentDetails.get());
		} else {
			throw new RuntimeException("Student with " + roll_no + " roll_no is not found");
		}
	}

	@Override
	public void updateStudentNameAndReturnDate(Long roll_no, Long bookId, String name, String returnDate) {
		StudentDetails studentDetails = detailsRepository.findById(roll_no)
				.orElseThrow(() -> new StudentNotFound("No Student With this Name"));

		BooksDetails booksDetails = bookDetailsRepository.findById(bookId)
				.orElseThrow(() -> new ResourceNotFoundException("No Book With this id founded"));

		System.out.println("Roll No: " + roll_no);
		System.out.println("Book ID: " + bookId);
		System.out.println("Name: " + name);
		System.out.println("Return Date: " + returnDate);

		studentDetails.setName(name);
		booksDetails.setReturnDate(LocalDateTime.parse(returnDate));
		detailsRepository.save(studentDetails);
		bookDetailsRepository.save(booksDetails);

	}
}
