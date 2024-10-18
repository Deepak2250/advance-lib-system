package com.ShelfSpace.ShelfSpace.exception;

public class BookAlreadyExist extends RuntimeException {

    public BookAlreadyExist() {
        super();
    }

    public BookAlreadyExist(String message) {
        super(message);
    }

    public BookAlreadyExist(String message, Throwable throwable) {
        super(message, throwable);
    }

}
