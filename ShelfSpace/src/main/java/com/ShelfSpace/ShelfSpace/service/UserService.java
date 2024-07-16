package com.ShelfSpace.ShelfSpace.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ShelfSpace.ShelfSpace.UserDao.UserInfoDao;
import com.ShelfSpace.ShelfSpace.customHandlers.MyPasswordEncoder;
import com.ShelfSpace.ShelfSpace.entites.User;
import com.ShelfSpace.ShelfSpace.entites.UserRole;
import com.ShelfSpace.ShelfSpace.repository.RoleRepository;
import com.ShelfSpace.ShelfSpace.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService implements UserInfoDao {

	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private MyPasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, RoleRepository roleRepository,
			MyPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	@Override
	public User addUser(User user) {

		Optional<User> userEmail = userRepository.findByEmail(user.getEmail());
		if (userEmail.isPresent()) {
			throw new RuntimeException("A user with this email already exists");
		}

		if (user.getRoles() == null || user.getRoles().isEmpty()) {
			List<UserRole> roles = roleRepository.findByRoleName("USER");
			user.setRoles(roles);
			user.setPassword(passwordEncoder.getPasswordEncoder().encode(user.getPassword()));

		}

		user.getRoles().forEach(userRole -> {
			userRole.setRegisteredUser(List.of(user));
		});

		return userRepository.save(user);
	}


}