package com.ShelfSpace.ShelfSpace.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "registered_user")
@NoArgsConstructor
@Getter
@Setter
public class User {

	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Id
	@Column(name = "email", nullable = false, unique = true)
	private String email;
	
	
	@Column(name = "password", nullable = false)
	private String password;

	@ManyToMany
	 @JoinTable(
		        name = "user_info_user_roles",
		        joinColumns = @JoinColumn(name = "user_info_email", referencedColumnName = "email"),
		        inverseJoinColumns = @JoinColumn(name = "user_roles_id")
		    )
	private List<UserRole> roles;

	public User(String name, String email, String password) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
	}
	
	

}
