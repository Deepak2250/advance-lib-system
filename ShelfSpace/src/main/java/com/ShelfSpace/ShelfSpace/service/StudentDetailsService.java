package com.ShelfSpace.ShelfSpace.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ShelfSpace.ShelfSpace.entites.BooksDetails;
import com.ShelfSpace.ShelfSpace.entites.StudentDetails;
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
	public Iterable<StudentDetails> getAllStudent() {
		return detailsRepository.findAll();
	}

	@Override
	public Optional<BooksDetails> deleteStudentBooks(Long book_id) {
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
	public Optional<BooksDetails> addBooks(BooksDetails booksDetails, Long roll_no) {
		Optional<StudentDetails> studentObj = detailsRepository.findById(roll_no);
		if (studentObj.isPresent()) {
			if (booksDetails.getBookId() != null) {
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
