package edu.wgu.c195.appointments.ui.calendar;

import edu.wgu.c195.appointments.domain.entities.Appointment;
import edu.wgu.c195.appointments.persistence.repositories.AppointmentRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CalendarController implements Initializable {


    @FXML
    private Button customersBtn;
    @FXML
    private Button newAppointmentBtn;
    @FXML
    private Button previousMonthBtn;
    @FXML
    private Button nextMonthBtn;
//    @FXML
//    private HBox calendarTitleContainer;
    @FXML
    private Text calendarTitle;
    @FXML
    private GridPane calendarHeader;
    @FXML
    private GridPane calendar;

    private YearMonth currentYearMonth;
    private List<Appointment> currentMonthsAppointments;
    private ResourceBundle resources;

    private final AppointmentRepository appointmentRepository;
    private final List<CalendarDayNode> calendarDays = new ArrayList<>(35);

    public CalendarController() {
        this.currentYearMonth = YearMonth.now();
        this.appointmentRepository = new AppointmentRepository();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        this.previousMonthBtn.setText("<<");
        this.nextMonthBtn.setText(">>");
        this.initializeTitleBar();
        this.initializeCalendarHeader();
        this.initializeCalendar();
    }

    private void initializeTitleBar() {
        this.calendarTitle.setText(" " + this.currentYearMonth.getMonth().toString() + " " + String.valueOf(this.currentYearMonth.getYear()) + " ");
    }

    private void initializeCalendarHeader() {
        Text[] dayNames = new Text[] {
          new Text(this.resources.getString("SundayHeaderTxt")),
          new Text(this.resources.getString("MondayHeaderTxt")),
          new Text(this.resources.getString("TuesdayHeaderTxt")),
          new Text(this.resources.getString("WednesdayHeaderTxt")),
          new Text(this.resources.getString("ThursdayHeaderTxt")),
          new Text(this.resources.getString("FridayHeaderTxt")),
          new Text(this.resources.getString("SaturdayHeaderTxt"))
        };
        int column = 0;
        for(Text dayName : dayNames) {
            AnchorPane ap = new AnchorPane();
            ap.setPrefSize(130, 10);
            ap.setBottomAnchor(dayName, 5.0);
            ap.getChildren().add(dayName);
            this.calendarHeader.add(ap, column++, 0);
        }
    }

    private void initializeCalendar() {
        this.calendar.setGridLinesVisible(true);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                CalendarDayNode ap = new CalendarDayNode();
                ap.setPrefSize(130,110);
                this.calendar.add(ap,j,i);
                this.calendarDays.add(ap);
            }
        }
        populateCalendar();
    }

    private void populateCalendar() {
        this.currentMonthsAppointments = this.appointmentRepository.getAll()
                .filter(a -> a.getStart()
                        .after(Date.valueOf(this.currentYearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM-01")))))
                .collect(Collectors.toList());
        LocalDate calendarDate = LocalDate.of(this.currentYearMonth.getYear(), this.currentYearMonth.getMonthValue(), 1);
        while (calendarDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
            calendarDate = calendarDate.minusDays(1);
        }

        for(CalendarDayNode node : this.calendarDays) {
            if(node.getChildren().size() != 0) {
                node.getChildren().remove(0);
            }
            Text calendarDateTxt = new Text(String.valueOf(calendarDate.getDayOfMonth()));
            ListView<String> calendarDateAppointments = new ListView<>();
            VBox calendarDateContainer = new VBox(calendarDateTxt, calendarDateAppointments);
            calendarDateContainer.setPrefSize(120, 100);
            node.setDate(calendarDate);
            node.setTopAnchor(calendarDateContainer, 5.0);
            node.setLeftAnchor(calendarDateContainer, 5.0);
            node.getChildren().add(calendarDateContainer);
            calendarDate = calendarDate.plusDays(1);
        }
    }

    public void previousMonth() {
        this.currentYearMonth = this.currentYearMonth.minusMonths(1);
        this.calendarTitle.setText(" " + this.currentYearMonth.getMonth().toString() + " " + String.valueOf(this.currentYearMonth.getYear()) + " ");
        this.populateCalendar();
    }

    public void nextMonth() {
        this.currentYearMonth = this.currentYearMonth.plusMonths(1);
        this.calendarTitle.setText(" " + this.currentYearMonth.getMonth().toString() + " " + String.valueOf(this.currentYearMonth.getYear()) + " ");
        this.populateCalendar();
    }

    public void navigateToManageCustomersView(ActionEvent actionEvent) {
        try {
            Stage primaryStage = (Stage) this.customersBtn.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("../customers/CustomersView.fxml"), this.resources);
            primaryStage.setScene(new Scene(root,960, 680));
        } catch (IOException e) {
            return;
        }
    }

    @FXML
    private void onNewAppointmentBtnClick(ActionEvent event) throws IOException {
        Stage primaryStage = (Stage) this.newAppointmentBtn.getScene().getWindow();
        final Stage createEditAppointmentStage = new Stage();
        createEditAppointmentStage.initModality(Modality.APPLICATION_MODAL);
        createEditAppointmentStage.initOwner(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource("../appointment/AppointmentView.fxml"));
        createEditAppointmentStage.setTitle("Create/Edit Appointment");
        createEditAppointmentStage.setScene(new Scene(root, 300, 200));
        createEditAppointmentStage.show();
    }
}
