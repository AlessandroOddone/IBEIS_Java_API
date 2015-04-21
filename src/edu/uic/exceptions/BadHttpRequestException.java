package edu.uic.exceptions;

public class BadHttpRequestException extends Exception {

    public BadHttpRequestException() {
        super();
    }

    public BadHttpRequestException(String message) {
        super(message);
    }
}