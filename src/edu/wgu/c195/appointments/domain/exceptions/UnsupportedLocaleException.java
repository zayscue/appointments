package edu.wgu.c195.appointments.domain.exceptions;

import java.util.Locale;

public class UnsupportedLocaleException extends RuntimeException {

    private final Locale currentLocale;

    public UnsupportedLocaleException(Locale currentLocale) {
        this.currentLocale = currentLocale;
    }

    @Override
    public String toString() {
        return "Unsupported Locale: " + this.currentLocale.toString();
    }
}
