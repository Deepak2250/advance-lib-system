package com.ShelfSpace.ShelfSpace.entites;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
	
	@OneToOne(mappedBy = "user" , cascade = CascadeType.ALL , orphanRemoval = true)
	private ForgotPassword forgotPassword;

	public User(String name, String email, String password) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
	}
	
	

}
