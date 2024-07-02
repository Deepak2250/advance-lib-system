package com.ShelfSpace.ShelfSpace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ShelfSpace.ShelfSpace.model.User;

public interface UserRepository extends JpaRepository<User, String> {

	User findByEmail(String email);
}
