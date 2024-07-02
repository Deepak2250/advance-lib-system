package com.ShelfSpace.ShelfSpace.service;

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
		
		User userDetails =  userRepository.findByEmail(email);
		if (userDetails==null) {
			throw new UsernameNotFoundException("No User Found");
		}
		
		 Collection<? extends GrantedAuthority> authorities = userDetails.getRoles().stream()
				 .map(authRoles -> new SimpleGrantedAuthority(authRoles.getRoleName()))
				 .collect(Collectors.toList());
		
		return new CustomUserDetails(userDetails.getEmail(), userDetails.getPassword(), authorities);
	}

}
