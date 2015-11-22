package edu.uic.ibeis_java_api.exceptions;

public class EmptyListParameterException extends Exception {

    public EmptyListParameterException() {
        super();
    }

    public EmptyListParameterException(String message) {
        super(message);
    }
}
