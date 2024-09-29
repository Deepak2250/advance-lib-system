package com.ShelfSpace.ShelfSpace.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ShelfSpace.ShelfSpace.UserDao.UserInfoDao;
import com.ShelfSpace.ShelfSpace.customHandlers.MyPasswordEncoder;
import com.ShelfSpace.ShelfSpace.entites.User;
import com.ShelfSpace.ShelfSpace.entites.UserRole;
import com.ShelfSpace.ShelfSpace.exception.PasswordNotCorrectException;
import com.ShelfSpace.ShelfSpace.model.ResetPassword;
import com.ShelfSpace.ShelfSpace.repository.RoleRepository;
import com.ShelfSpace.ShelfSpace.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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

	@Override
	public boolean resetPassword(ResetPassword resetPassword , User user) {
		
			if (resetPassword.getNewPassword().equals(resetPassword.getRepeatPassword())) {
				log.warn("SuccessFully Both are good and same");
				String encryptPassword = passwordEncoder.getPasswordEncoder().encode(resetPassword.getNewPassword());
				userRepository.resetPassword( encryptPassword , user.getEmail());
				log.warn("SuccessFully Changed the Password");
				return true;
			}
			else {
				log.error("The Password Not Correct Exception :" , resetPassword.getNewPassword());
				throw new PasswordNotCorrectException("The Password You have Provided is not Valid");
				
			}
	}

}