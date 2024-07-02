package com.ShelfSpace.ShelfSpace.rest.model.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ShelfSpace.ShelfSpace.model.CustomUserDetails;
import com.ShelfSpace.ShelfSpace.model.User;
import com.ShelfSpace.ShelfSpace.repository.UserRepository;

public class UserDetailsServiceImp implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) {
		
		User userEntity =  userRepository.findByEmail(email);
		if (userEntity == null) {
			throw new UsernameNotFoundException("No User Found");
		}
		
		 Collection<? extends GrantedAuthority> authorities = userEntity.getRoles().stream()
	                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
	                .collect(Collectors.toList());
		 
		 
		return new CustomUserDetails(userEntity.getEmail() , userEntity.getPassword() , authorities);
	}

}
