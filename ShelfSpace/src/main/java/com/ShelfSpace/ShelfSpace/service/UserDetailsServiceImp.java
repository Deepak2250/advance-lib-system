package com.ShelfSpace.ShelfSpace.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.ShelfSpace.ShelfSpace.customHandlers.CustomUserDetails;
import com.ShelfSpace.ShelfSpace.entites.User;
import com.ShelfSpace.ShelfSpace.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class UserDetailsServiceImp implements UserDetailsService{

	
	@Autowired
	private UserRepository userRepository;

	
	@Override
	@Transactional // If we don't use This Then The roles will give us the exception LazyInitializationException bcz the userDetails.getRoles()
	               // is Lazy loaded by default because its one to many mapping and to get it we had to explicity call it via query and if we 
	               // we add the @Transactional Then the Transaction manager will handle it internally .. & ensures that a session is available when accessing lazy-loaded collections
	               // Either we have to do like @OneToMany(Fetch.Type = Eager)
	public UserDetails loadUserByUsername(String email) {
		log.warn("Entered in the LoadUsername ");
		Optional<User> userDetails =  userRepository.findByEmail(email);
		if (userDetails.isEmpty()) {
			log.error("The user is not there ", email);
			throw new RuntimeException("No User Found");
		}
		
	
		log.warn("The user with the email " , email , "hass been founded" );
		 Collection<? extends GrantedAuthority> authorities = userDetails.get().getRoles().stream()
				 .map(authRoles -> new SimpleGrantedAuthority(authRoles.getRoleName()))
				 .collect(Collectors.toList());

		return new CustomUserDetails(userDetails.get().getEmail(), userDetails.get().getPassword(), authorities);
	}



}