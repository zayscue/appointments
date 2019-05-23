package edu.wgu.c195.appointments.domain.exceptions;

public class ConflictingAppointmentException extends Exception {

    private static final String DEFAULT_ERROR_MESSAGE = "This appointment conflicts with another existing appointment.";

    public ConflictingAppointmentException() {
        super(DEFAULT_ERROR_MESSAGE);
    }
}
