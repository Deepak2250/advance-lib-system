package com.ShelfSpace.ShelfSpace.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ShelfSpace.ShelfSpace.model.User;

public interface UserRepository extends JpaRepository<User, String> {

	Optional<User> findByEmail(String email);
}
