package edu.wgu.c195.appointments.domain.exceptions;

import edu.wgu.c195.appointments.domain.ValidationResult;

public class InvalidAppointmentDataException extends Exception {

    private static final String DEFAULT_ERROR_MESSAGE = "The appointment record you have tried to insert or update is invalid.";

    public InvalidAppointmentDataException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public InvalidAppointmentDataException(ValidationResult validationResult) {
        super(errorMessage(validationResult));
    }

    private static String errorMessage(ValidationResult validationResult) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DEFAULT_ERROR_MESSAGE);
        stringBuilder.append("\n");
        stringBuilder.append("The following are the reason(s) why: \n");
        for (String error : validationResult.getErrors()) {
            stringBuilder.append(error);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
