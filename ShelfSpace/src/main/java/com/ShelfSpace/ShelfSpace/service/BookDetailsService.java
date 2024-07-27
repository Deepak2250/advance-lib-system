// package com.ShelfSpace.ShelfSpace.service;

// import com.ShelfSpace.ShelfSpace.model.GoogleResponsePojo;
// import com.ShelfSpace.ShelfSpace.model.StudentDetailsPojo;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// @Service
// public class BookDetailsService {

// @Autowired
// private ApiService apiService;

// private GoogleResponsePojo bookDetails;
// private StudentDetailsPojo detailsPojo;

// public StudentDetailsPojo gStudentDetailsPojo() {
// return detailsPojo;
// }

// public void init() {
// if (detailsPojo == null) {
// throw new IllegalStateException("detailsPojo is not initialized");
// }

// this.bookDetails =
// apiService.getBookDetails(detailsPojo.getBooksDetails().get(0).getBookName());
// printBookDetails();
// }

// public GoogleResponsePojo getBookDetails() {
// return bookDetails;
// }

// private void printBookDetails() {
// if (bookDetails != null) {
// System.out.println("Subtitle: " + bookDetails.getSubtitle());
// System.out.println("Authors: " + bookDetails.getAuthors());
// System.out.println("Published Date: " + bookDetails.getPublishedDate());
// System.out.println("Description: " + bookDetails.getDescription());
// System.out.println("Image : " + bookDetails.getCureencyCode());
// System.out.println("Currency code : " + bookDetails.getImage());
// System.out.println("Synposis : " + bookDetails.getSynposis());
// System.out.println("Pdf: " + bookDetails.getBookPdf());

// }
// }
// }
