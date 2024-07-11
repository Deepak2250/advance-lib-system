package com.ShelfSpace.ShelfSpace.customHandlers;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class CustomUserDetails implements UserDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String email;
	private final String password;
	private final Collection<? extends GrantedAuthority> authorities;

	
	 public CustomUserDetails(String email, String password, Collection<? extends GrantedAuthority> authorities) {
	     
			this.email = email;
	        this.password = password;
	        this.authorities = authorities;
	    }
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return email;
	}

}
