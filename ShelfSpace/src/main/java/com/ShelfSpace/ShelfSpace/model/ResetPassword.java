package com.ShelfSpace.ShelfSpace.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ResetPassword {

	@NotNull(message = "The Field Should Be Not Null")
	@Size(min = 3 , message = "Minimum Name Should Be more than 3 letters")
	private String newPassword;
	
	@NotNull(message = "The Field Should Be Not Null")
	@Size(min = 3 , message = "Minimum Name Should Be more than 3 letters")
	private String repeatPassword;
}
