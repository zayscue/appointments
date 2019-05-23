package edu.wgu.c195.appointments.domain;

import java.util.List;

public class ValidationResult {

    private final boolean isValid;

    public boolean isValid() {
        return isValid;
    }

    public List<String> getErrors() {
        return errors;
    }

    private final List<String> errors;

    public ValidationResult() {
        this.isValid = true;
        this.errors = null;
    }

    public ValidationResult(List<String> errors) {
        this.isValid = false;
        this.errors = errors;
    }
}
