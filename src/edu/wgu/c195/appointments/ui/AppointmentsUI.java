package edu.wgu.c195.appointments.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AppointmentsUI extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Parent root = FXMLLoader.load(getClass().getResource("./login/LoginView.fxml"));
        stage.setTitle("FXML Welcome (java: " + javaVersion + " javafx: " + javafxVersion + ")");
        stage.setScene(new Scene(root, 640, 480));
        stage.show();
    }

}