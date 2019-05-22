package edu.wgu.c195.appointments.ui.calendar;

import edu.wgu.c195.appointments.persistence.repositories.AppointmentRepository;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private VBox calendarVBox;
    @FXML
    private Button customersBtn;
    @FXML
    private Button newAppointmentBtn;
    @FXML
    private ToggleGroup calendarViewOptions;

    private ResourceBundle resources;
    private Node monthlyCalendar;
    private final CalendarView monthlyController;
    private Node weeklyCalendar;
    private final CalendarView weeklyController;

    private final AppointmentRepository appointmentRepository;

    public MainController() {
        this.appointmentRepository = new AppointmentRepository();
        this.monthlyController = new MonthlyCalendarController(this.appointmentRepository);
        this.weeklyController = new WeeklyCalendarController(this.appointmentRepository);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        this.calendarViewOptions.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            this.loadCalendar();
        });
        this.loadCalendar();
    }

    private void loadCalendar() {
        ToggleButton selectedToggleButton = (ToggleButton) this.calendarViewOptions.getSelectedToggle();
        switch (selectedToggleButton.getText()) {
            case "Week":
                loadWeeklyCalendar();
                break;
            case "Month":
            default:
                loadMonthlyCalendar();
                break;
        }
    }

    private void loadMonthlyCalendar() {
        if (this.monthlyCalendar == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("./MonthlyCalendarView.fxml"), resources);
            loader.setController(this.monthlyController);
            try {
                this.monthlyCalendar = loader.load();
                VBox.setVgrow(this.monthlyCalendar, Priority.ALWAYS);
            } catch (IOException e) {
                StackPane errorPane = new StackPane();
                errorPane.getChildren().add(new Text(e.getMessage()));
                this.monthlyCalendar = errorPane;
            }
        }
        if (this.weeklyCalendar != null) {
            this.calendarVBox.getChildren().remove(this.weeklyCalendar);
        }
        this.calendarVBox.getChildren().add(this.monthlyCalendar);
    }

    private void loadWeeklyCalendar() {
        if (this.weeklyCalendar == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("./WeeklyCalendarView.fxml"), resources);
            loader.setController(this.weeklyController);
            try {
                this.weeklyCalendar = loader.load();
                VBox.setVgrow(this.weeklyCalendar, Priority.ALWAYS);
            } catch (IOException e) {
                StackPane errorPane = new StackPane();
                errorPane.getChildren().add(new Text(e.getMessage()));
                this.weeklyCalendar = errorPane;
            }
        }
        if (this.monthlyCalendar != null) {
            this.calendarVBox.getChildren().remove(this.monthlyCalendar);
        }
        this.calendarVBox.getChildren().add(this.weeklyCalendar);
    }

    public void navigateToManageCustomersView(ActionEvent actionEvent) throws IOException {
        Stage primaryStage = (Stage) this.customersBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../customers/CustomersView.fxml"), this.resources);
        primaryStage.setScene(new Scene(root,primaryStage.getWidth(), primaryStage.getHeight()));
    }

    @FXML
    private void onNewAppointmentBtnClick(ActionEvent event) throws IOException {
        Stage primaryStage = (Stage) this.newAppointmentBtn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../appointment/AppointmentView.fxml"), resources);
        launchCreateEditAppointmentStage(primaryStage, loader);
    }

    private void launchCreateEditAppointmentStage(Stage owner, FXMLLoader loader) throws IOException {
        ToggleButton selectedToggleButton = (ToggleButton) this.calendarViewOptions.getSelectedToggle();
        final String calendarViewOption = selectedToggleButton.getText();
        final CalendarView monthlyCalendarView = this.monthlyController;
        final CalendarView weeklyCalendarView = this.weeklyController;
        final Stage createEditAppointmentStage = new Stage();
        createEditAppointmentStage.initModality(Modality.APPLICATION_MODAL);
        createEditAppointmentStage.initOwner(owner);
        Parent root = loader.load();
        createEditAppointmentStage.setTitle("Create/Edit Appointment");
        createEditAppointmentStage.setScene(new Scene(root, 560, 580));
        createEditAppointmentStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                switch (calendarViewOption) {
                    case "Week":
                        weeklyCalendarView.reloadCalendar();
                        break;
                    case "Month":
                    default:
                        monthlyCalendarView.reloadCalendar();
                        break;
                }
            }
        });
        createEditAppointmentStage.show();
    }
}
