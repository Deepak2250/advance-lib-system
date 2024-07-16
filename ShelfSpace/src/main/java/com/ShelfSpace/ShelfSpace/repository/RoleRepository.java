package com.ShelfSpace.ShelfSpace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ShelfSpace.ShelfSpace.entites.UserRole;

import java.util.List;


public interface RoleRepository extends JpaRepository<UserRole, Integer>{

	List<UserRole> findByRoleName(String roleName);
}
