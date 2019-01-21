package edu.wgu.c195.appointments.ui;

import edu.wgu.c195.appointments.domain.UnsupportedLocaleException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

public class AppointmentsUI extends Application {

    private final Locale[] supportedLocales = {
            new Locale("es"),
            Locale.GERMAN,
            Locale.ENGLISH
    };

    private ResourceBundle labels;

    @Override
    public void start(Stage stage) throws IOException, UnsupportedLocaleException {
        Locale currentLocale = Locale.getDefault();
        boolean supportedLocale = Arrays.stream(supportedLocales)
                .anyMatch(locale -> locale.getLanguage().equals(currentLocale.getLanguage()));
        if(!supportedLocale) {
            throw new UnsupportedLocaleException(currentLocale);
        }
        this.labels = ResourceBundle.getBundle("LabelsBundle", currentLocale);
        String windowTitle = labels.getString("windowTitle");
        Parent root = FXMLLoader.load(getClass().getResource("./login/LoginView.fxml"));
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(root, 640, 480));
        stage.show();
    }

}