package edu.wgu.c195.appointments.ui;

import edu.wgu.c195.appointments.domain.entities.User;
import edu.wgu.c195.appointments.domain.exceptions.UnsupportedLocaleException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.VBox;
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
    public static User CurrentUser;

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
        final Menu menu1 = new Menu("File");
        final Menu menu2 = new Menu("Options");
        final Menu menu3 = new Menu("Help");

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menu1, menu2, menu3);

        String windowTitle = AppointmentsUI.Resources.getString("windowTitle");
        Parent root = FXMLLoader.load(getClass().getResource("./login/LoginView.fxml"), AppointmentsUI.Resources);
        VBox vb = new VBox(menuBar, root);
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(vb, 960, 680));
        stage.show();
    }

}