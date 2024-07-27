package com.ShelfSpace.ShelfSpace.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ShelfSpace.ShelfSpace.entites.BooksDetails;

public interface BookDetailsRepository extends JpaRepository<BooksDetails, Long> {

    @Query(value = "SELECT b.bookId FROM BooksDetails b WHERE b.bookId = :bookId")
    Optional<BooksDetails> findByBookId(@Param("bookId") Long bookId);
}
