package com.ShelfSpace.ShelfSpace.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {

	@Id
	@Column(name = "role_id")
   @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int roleId;
	
	 @Column(name = "role_name", nullable = false, unique = true)
	private String roleName;

	 @ManyToMany(mappedBy = "roles")
	private List<User> registeredUser;
}