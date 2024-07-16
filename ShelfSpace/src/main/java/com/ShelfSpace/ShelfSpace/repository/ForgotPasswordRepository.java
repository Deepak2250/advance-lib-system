package com.ShelfSpace.ShelfSpace.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ShelfSpace.ShelfSpace.entites.ForgotPassword;
import com.ShelfSpace.ShelfSpace.entites.User;


public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Long> {

	@Query("SELECT fp FROM ForgotPassword fp WHERE fp.otp = :otp AND fp.user = :user")
	Optional<ForgotPassword> findByOtpAndUser(int otp, User user);
	
	@Query("SELECT fp FROM ForgotPassword fp WHERE fp.user = :user")
	ForgotPassword findByUserObj(@Param("user") User user);

	
 
}
