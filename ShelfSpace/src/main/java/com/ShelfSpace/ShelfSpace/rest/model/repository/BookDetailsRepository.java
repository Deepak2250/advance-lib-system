package com.ShelfSpace.ShelfSpace.rest.model.repository;

import java.util.Optional;

import org.aspectj.weaver.tools.Trace;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ShelfSpace.ShelfSpace.rest.model.BooksDetails;

public interface BookDetailsRepository extends CrudRepository<BooksDetails, Long> {
	
	@Query(value = "SELECT book_id FROM issued_books WHERE book_id = ?1 LIMIT 1", nativeQuery = true)
    Optional<BooksDetails> findByBookId(Long bookId);
}
