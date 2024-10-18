package com.ShelfSpace.ShelfSpace.exception;

public class StudentNotFound extends RuntimeException {

    public StudentNotFound() {
        super();
    }

    public StudentNotFound(String message) {
        super(message);
    }

    public StudentNotFound(String message, Throwable throwable) {
        super(message, throwable);
    }

}
