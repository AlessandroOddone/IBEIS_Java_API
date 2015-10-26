package edu.uic.ibeis_java_api.exceptions;

public class MalformedHttpRequestException extends Exception {

    public MalformedHttpRequestException() {
        super();
    }

    public MalformedHttpRequestException(String message) {
        super(message);
    }
}