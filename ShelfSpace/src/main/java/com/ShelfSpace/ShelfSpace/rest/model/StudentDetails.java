package com.ShelfSpace.ShelfSpace.rest.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "User_Info")
public class StudentDetails {
	
	@Id
    private Long roll_no;

    private String name;
    private String email;
    private String phoneNumber;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<BooksDetails> booksDetails;
    
    
	public StudentDetails() {}
	
	public StudentDetails(Long roll_no, String name, String email, String phoneNumber, List<BooksDetails> booksDetails) {
	
		this.roll_no = roll_no;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.booksDetails = booksDetails;
	}

	public Long getRoll_no() {
		return roll_no;
	}

	public void setRoll_no(Long roll_no) {
		this.roll_no = roll_no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<BooksDetails> getBooksDetails() {
		return booksDetails;
	}

	public void setBooksDetails(List<BooksDetails> booksDetails) {
		this.booksDetails = booksDetails;
	}
	
	
	
    
}
