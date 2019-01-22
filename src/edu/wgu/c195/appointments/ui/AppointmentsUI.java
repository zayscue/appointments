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

    private static final Locale[] SUPPORTED_LOCALES = {
            new Locale("es"),
            Locale.GERMAN,
            Locale.ENGLISH
    };

    public final static ResourceBundle Resources;

    static {
        Locale currentLocale = Locale.getDefault();
        boolean supportedLocale = Arrays.stream(SUPPORTED_LOCALES)
                .anyMatch(locale -> locale.getLanguage().equals(currentLocale.getLanguage()));
        if(!supportedLocale) {
            throw new UnsupportedLocaleException(currentLocale);
        }
        Resources = ResourceBundle.getBundle("LabelsBundle", currentLocale);
    }

    @Override
    public void start(Stage stage) throws IOException, UnsupportedLocaleException {
        String windowTitle = AppointmentsUI.Resources.getString("windowTitle");
        Parent root = FXMLLoader.load(getClass().getResource("./login/LoginView.fxml"), AppointmentsUI.Resources);
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(root, 640, 480));
        stage.show();
    }

}