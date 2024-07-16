package com.ShelfSpace.ShelfSpace.exception;

public class EmailNotFound extends RuntimeException {

	
	private static final long serialVersionUID = 1L;

	public EmailNotFound() {
		super("Email is Not Found");

	}

	public EmailNotFound(String Message) {
		super(Message);
	}
	
	public EmailNotFound(String Message , Throwable cauese) {
		super(Message , cauese);
	}

}
