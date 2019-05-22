package edu.wgu.c195.appointments.ui.login;

import edu.wgu.c195.appointments.application.LoginLogger;
import edu.wgu.c195.appointments.application.UserManager;
import edu.wgu.c195.appointments.domain.entities.User;
import edu.wgu.c195.appointments.domain.exceptions.IncorrectUserNameOrPasswordException;

import edu.wgu.c195.appointments.ui.AppointmentsUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Text welcomeLabel;
    @FXML
    private Label userNameLabel;
    @FXML
    private TextField userNameField;
    @FXML
    private Label passwordLabel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button signBtn;

    private ResourceBundle bundle;
    private UserManager userManager;

    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        String userName = this.userNameField.getText();
        String password = this.passwordField.getText();

        try
        {
            if((userName != null && !userName.equals("")) && (password != null && !password.equals(""))) {
                User user = this.userManager.findByUserName(userName);
                if(user != null) {
                    if(this.userManager.checkPassword(user, password)) {
                        Thread loggingThread = new Thread(LoginLogger.createLogger(user));
                        loggingThread.start();
                        AppointmentsUI.CurrentUser = user;
                        Stage primaryStage = (Stage) this.signBtn.getScene().getWindow();
                        Parent root = FXMLLoader.load(getClass().getResource("../calendar/MainView.fxml"), this.bundle);
                        primaryStage.setScene(new Scene(root,960, 680));
                    } else {
                        throw new IncorrectUserNameOrPasswordException();
                    }
                } else {
                    throw new IncorrectUserNameOrPasswordException();
                }
            }
        }
        catch(IncorrectUserNameOrPasswordException loginException) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Ooops, there was an error!");
            alert.setContentText(loginException.getMessage());

            alert.showAndWait();
        }
        catch(IOException e) {
            return;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.bundle = resources;
        this.userManager = new UserManager();
        this.welcomeLabel.setText(this.bundle.getString("welcomeLabel"));
        this.userNameLabel.setText(String.format("%s:", this.bundle.getString("userNameLabel")));
        this.passwordLabel.setText(String.format("%s:", this.bundle.getString("passwordLabel")));
        this.signBtn.setText(this.bundle.getString("signInBtnTxt"));
    }
}
