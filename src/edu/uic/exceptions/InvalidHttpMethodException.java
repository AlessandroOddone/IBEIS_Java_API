package edu.uic.exceptions;

public class InvalidHttpMethodException extends Exception {

    public InvalidHttpMethodException() {
        super();
    }

    public InvalidHttpMethodException(String message) {
        super(message);
    }
}
