package com.ShelfSpace.ShelfSpace.entites;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "student_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDetails {

	private String name;

	@Id
	private Long roll_no;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private Long phoneNumber;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<BooksDetails> booksDetails;

}