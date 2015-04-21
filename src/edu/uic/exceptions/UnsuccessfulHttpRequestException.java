package edu.uic.exceptions;

public class UnsuccessfulHttpRequestException extends Exception {

    public UnsuccessfulHttpRequestException() {
        super();
    }

    public UnsuccessfulHttpRequestException(String message) {
        super(message);
    }
}
