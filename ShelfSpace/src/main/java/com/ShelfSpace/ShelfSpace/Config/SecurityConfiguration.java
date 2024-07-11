package com.ShelfSpace.ShelfSpace.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import com.ShelfSpace.ShelfSpace.customHandlers.CustomAuthenticationHandler;
import com.ShelfSpace.ShelfSpace.customHandlers.CustomLogoutHandler;
import com.ShelfSpace.ShelfSpace.customHandlers.MyPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	 @Autowired
	 private MyPasswordEncoder passwordEncoder;  //// For Converting the Encrypted Password to The String format
	 @Autowired
	 private UserDetailsService userDetailsService;; /// For fetching The user details from a custom source

	 @Autowired
	    private CustomAuthenticationHandler customAuthenticationHandler;
	 
    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/", "/signup" , "/processRegister" , "/Login").permitAll()
                .anyRequest().authenticated())

                .formLogin(login -> login.loginPage("/Login")
                		.loginProcessingUrl("/LoginPage")
                		.usernameParameter("email")
                		.passwordParameter("password").failureHandler(customAuthenticationHandler)/// Also Added The Username Parameter as the Email and password Parameter is common is password
                        .defaultSuccessUrl("/" , true).permitAll())
				.logout(logout -> logout.logoutUrl("/logout")
						.logoutSuccessHandler
						(new CustomLogoutHandler())
						.permitAll());

        return httpSecurity.build();
    }
    
    //@Bean
    //AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    //     return authenticationConfiguration.getAuthenticationManager();
    //}
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder.getPasswordEncoder());
    }
     
}
