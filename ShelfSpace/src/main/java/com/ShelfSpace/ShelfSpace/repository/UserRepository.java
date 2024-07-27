package com.ShelfSpace.ShelfSpace.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ShelfSpace.ShelfSpace.entites.User;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, String> {

	Optional<User> findByEmail(String email);
	
	@Transactional
	@Modifying
	@Query("UPDATE User user SET user.password = :password WHERE user.email = :email")
	int resetPassword(@Param("password")String password , @Param("email") String email);
}
