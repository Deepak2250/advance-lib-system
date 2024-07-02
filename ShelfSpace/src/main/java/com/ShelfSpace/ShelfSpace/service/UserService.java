package com.ShelfSpace.ShelfSpace.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ShelfSpace.ShelfSpace.UserDao.UserInfoDao;
import com.ShelfSpace.ShelfSpace.model.User;
import com.ShelfSpace.ShelfSpace.model.UserRole;
import com.ShelfSpace.ShelfSpace.repository.RoleRepository;
import com.ShelfSpace.ShelfSpace.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService implements UserInfoDao {

	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;

	@Autowired
	public UserService(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	@Transactional
	@Override
	public User addUser(User user) {
		if (user.getRoles() == null || user.getRoles().isEmpty()) {
			List<UserRole> roles = roleRepository.findByRoleName("USER");
			user.setRoles(roles);
			user.setPassword(passwordEncoder.encode(user.getPassword()));

		}

		user.getRoles().forEach(userRole -> {
			userRole.setRegisteredUser(List.of(user));
		});

		userRepository.save(user);
		return user;
	}


}