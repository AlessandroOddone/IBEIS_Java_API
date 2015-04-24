package edu.uic.ibeis_java_api.exceptions;

public class UnsuccessfulHttpRequestException extends Exception {

    public UnsuccessfulHttpRequestException() {
        super();
    }

    public UnsuccessfulHttpRequestException(String message) {
        super(message);
    }
}
