package com.ShelfSpace.ShelfSpace.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.ShelfSpace.ShelfSpace.service.UserDetailsServiceImp;

@Configuration
public class SecurityConfiguration {
	
	@Autowired
	private UserDetailsServiceImp detailsServiceImp;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth.requestMatchers("/", "/signup" , "/processRegister").permitAll()
                .anyRequest().authenticated())

				.formLogin(login -> login.loginPage("/LoginPage").loginProcessingUrl("/LoginPage")
						.defaultSuccessUrl("/").permitAll())
				.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll());

		return httpSecurity.build();
	}
	
	 @Autowired
	    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	        auth
	            .userDetailsService(detailsServiceImp).passwordEncoder(encoder());
	    }

	@Bean
	PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
