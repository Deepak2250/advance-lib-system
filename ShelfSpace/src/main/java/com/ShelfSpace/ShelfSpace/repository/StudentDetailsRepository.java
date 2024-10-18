package com.ShelfSpace.ShelfSpace.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ShelfSpace.ShelfSpace.entites.StudentDetails;

public interface StudentDetailsRepository extends JpaRepository<StudentDetails, Long> {

    @Query("SELECT s.roll_no FROM StudentDetails s WHERE s.roll_no = :roll_no")
    Optional<StudentDetails> findByRollNo(@Param("roll_no") Long roll_no);

    @Query("SELECT s FROM StudentDetails s WHERE s.email = :email")
    Optional<StudentDetails> findByEmail(@Param("email") String email);
}
