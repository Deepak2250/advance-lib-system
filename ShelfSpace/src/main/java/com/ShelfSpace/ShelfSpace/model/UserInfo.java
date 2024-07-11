package com.ShelfSpace.ShelfSpace.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class UserInfo {

	@jakarta.validation.constraints.NotNull(message = "The Field Should Be Not Null")
	@Size(min = 3 , message = "Minimum Name Should Be more than 3 letters")
	private String name;
	
	@Email(regexp = "[A-Za-z0-9\\._%+\\-]+@[A-Za-z0-9\\.\\-]+\\.[A-Za-z]{2,}" , message = "The Email is not Valid")
	@NotNull(message = "The Field Should Be Not Null")
	private String email;
	
	@NotNull(message = "The Field Should Be Not Null")
	@Size(min = 3 , message = "Minimum Name Should Be more than 3 letters")
	private String password;


}
