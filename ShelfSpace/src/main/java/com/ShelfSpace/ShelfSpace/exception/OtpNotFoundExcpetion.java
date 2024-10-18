package com.ShelfSpace.ShelfSpace.exception;

public class OtpNotFoundExcpetion extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OtpNotFoundExcpetion() {
		super("Opt Not Found");
	}
	
	public OtpNotFoundExcpetion(String message) {
		super(message);
	}
	
	public OtpNotFoundExcpetion(String message , Throwable throwable) {
		super(message, throwable);
	}
}
