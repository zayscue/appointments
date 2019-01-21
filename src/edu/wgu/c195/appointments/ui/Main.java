package edu.wgu.c195.appointments.ui;

import javafx.application.Application;

import java.util.Locale;
import java.util.ResourceBundle;


public class Main {

    public static void main(String[] args) {
        Locale deLocale = new Locale("de");
        ResourceBundle labels = ResourceBundle.getBundle("LabelsBundle", deLocale);
        String s2 = labels.getString("s2");
        System.out.println(s2);

        Application.launch(AppointmentsUI.class, args);
    }
}
