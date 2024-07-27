package com.ShelfSpace.ShelfSpace.entites;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "book_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BooksDetails {

    @Id
    private Long bookId;

    private String bookName;

    @DateTimeFormat
    private LocalDateTime issueDate;

    @DateTimeFormat
    private LocalDateTime returnDate;

    @JsonIgnore
    private String subtitle;

    @JsonIgnore
    private String authors;

    @JsonIgnore
    private String publishDate;

    @JsonIgnore
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @JsonIgnore
    private String image;

    @JsonIgnore
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String synposis;

    @JsonIgnore
    private String price;

    @JsonIgnore
    private String currencyCode;

    @JsonIgnore
    private String bookPdf;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_roll_no")
    private StudentDetails user;
}