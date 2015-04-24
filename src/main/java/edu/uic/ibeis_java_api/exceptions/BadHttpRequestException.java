package edu.uic.ibeis_java_api.exceptions;

public class BadHttpRequestException extends Exception {

    public BadHttpRequestException() {
        super();
    }

    public BadHttpRequestException(String message) {
        super(message);
    }
}