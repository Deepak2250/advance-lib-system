package com.ShelfSpace.ShelfSpace.exception;

import org.springframework.dao.DuplicateKeyException;

public class OtpAlreadyPresent extends DuplicateKeyException{
	
	private static final long serialVersionUID = -4336603323209991589L;

	public OtpAlreadyPresent(String msg) {
		super(msg);
	}
	
	public OtpAlreadyPresent(String msg , Throwable throwable) {
		super(msg , throwable);
	}


}
