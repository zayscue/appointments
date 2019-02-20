package edu.wgu.c195.appointments.domain.exceptions;

public class IncorrectUserNameOrPasswordException extends Exception {
    private static final String DEFAULT_ERROR_MESSAGE = "The username or password entered do not match our records.";

    public IncorrectUserNameOrPasswordException() {
        super(DEFAULT_ERROR_MESSAGE);
    }
}
