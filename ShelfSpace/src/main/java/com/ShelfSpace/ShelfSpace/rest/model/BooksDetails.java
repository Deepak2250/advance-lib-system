package com.ShelfSpace.ShelfSpace.rest.model;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Issued_Books")
public class BooksDetails {

    @Id
    private Long bookId;
    private String bookName;
    @DateTimeFormat
    private LocalDateTime issueDate;
    @DateTimeFormat
    private LocalDateTime returnDate;
    
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_roll_no")
    private StudentDetails user;

    public BooksDetails() {
    }

    public BooksDetails(StudentDetails user, Long bookId , String bookName) {
        this.user = user;
        this.bookId = bookId;
        this.bookName = bookName;
    }

    public StudentDetails getUser() {
        return user;
    }

    public void setUser(StudentDetails user) {
        this.user = user;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }
}