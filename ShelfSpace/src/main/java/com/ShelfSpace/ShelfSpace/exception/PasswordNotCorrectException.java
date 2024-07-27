package com.ShelfSpace.ShelfSpace.exception;

public class PasswordNotCorrectException extends RuntimeException {
   
	private static final long serialVersionUID = 1L;

	public PasswordNotCorrectException(String message) {
        super(message);
    }
	
	public PasswordNotCorrectException(String message , Throwable throwable) {
        super(message , throwable);
    }
}
