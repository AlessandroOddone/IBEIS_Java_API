package edu.uic.exceptions;

public class UnsuccessfulHttpRequest extends Exception {

    public UnsuccessfulHttpRequest() {
        super();
    }

    public UnsuccessfulHttpRequest(String message) {
        super(message);
    }
}
