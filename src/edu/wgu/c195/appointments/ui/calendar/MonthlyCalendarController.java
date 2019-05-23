package edu.wgu.c195.appointments.ui.calendar;

import edu.wgu.c195.appointments.domain.entities.Appointment;
import edu.wgu.c195.appointments.persistence.repositories.AppointmentRepository;
import edu.wgu.c195.appointments.ui.appointment.AppointmentController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MonthlyCalendarController extends CalendarView implements Initializable {

    private final AppointmentRepository  appointmentRepository;
    private final List<CalendarDayNode> dayNodes = new ArrayList<>(35);

    @FXML
    private Button previousMonthBtn;
    @FXML
    private Text monthText;
    @FXML
    private Button nextMonthBtn;
    @FXML
    private GridPane monthlyCalendarHeader;
    @FXML
    private GridPane monthlyCalendar;
    private ResourceBundle resources;
    private YearMonth month;


    public MonthlyCalendarController(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
        this.month = YearMonth.now();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        this.previousMonthBtn.setText("<<");
        this.previousMonthBtn.setOnAction(this::navigateToPreviousMonth);
        this.nextMonthBtn.setText(">>");
        this.nextMonthBtn.setOnAction(this::navigateToNextMonth);
        this.changeMonthText();
        this.initializeMonthlyCalendarHeader();
        this.intializeMonthlyCalendar();
    }

    private void changeMonthText() {
        this.monthText.setText(" " + this.month.getMonth().toString() + " " + String.valueOf(this.month.getYear()) + " ");
    }

    private void initializeMonthlyCalendarHeader() {
        Text[] dayNames = new Text[] {
            new Text(this.resources.getString("SundayHeaderTxt").trim()),
            new Text(this.resources.getString("MondayHeaderTxt").trim()),
            new Text(this.resources.getString("TuesdayHeaderTxt").trim()),
            new Text(this.resources.getString("WednesdayHeaderTxt").trim()),
            new Text(this.resources.getString("ThursdayHeaderTxt").trim()),
            new Text(this.resources.getString("FridayHeaderTxt").trim()),
            new Text(this.resources.getString("SaturdayHeaderTxt").trim())
        };
        int column = 0;
        for(Text dayName : dayNames) {
            StackPane sp = new StackPane();
            sp.setPrefHeight(10);
            GridPane.setHgrow(sp, Priority.ALWAYS);
            sp.getChildren().add(dayName);
            this.monthlyCalendarHeader.add(sp, column++, 0);
        }
    }

    private void intializeMonthlyCalendar() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                CalendarDayNode ap = new CalendarDayNode();
                this.monthlyCalendar.add(ap,j,i);
                GridPane.setHgrow(ap, Priority.ALWAYS);
                GridPane.setVgrow(ap, Priority.ALWAYS);
                VBox.setVgrow(ap, Priority.ALWAYS);
                this.dayNodes.add(ap);
            }
        }
        renderMonthlyCalendar();
    }

    private void renderMonthlyCalendar() {
        List<Appointment> appointments = this.appointmentRepository.getAll()
            .filter(a -> a.getStart().after(Date.valueOf(this.month.format(DateTimeFormatter.ofPattern("yyyy-MM-01"))))
                    && a.getStart().before(Date.valueOf(LocalDate.of(this.month.getYear(), this.month.getMonthValue() + 1, 1)))
            )
            .sorted(new Comparator<Appointment>() {
                @Override
                public int compare(Appointment o1, Appointment o2) {
                    if(o1.getStart().after(o2.getStart())) {
                        return 1;
                    } else if (o1.getStart().before(o2.getStart())) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            })
            .collect(Collectors.toList());

        LocalDate calendarDate = LocalDate.of(this.month.getYear(), this.month.getMonthValue(), 1);
        while (calendarDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
            calendarDate = calendarDate.minusDays(1);
        }

        for (CalendarDayNode dayNode : this.dayNodes) {
            if (dayNode.getChildren().size() > 0) {
                dayNode.getChildren().remove(0);
            }

            final LocalDate date = LocalDate.of(calendarDate.getYear(), calendarDate.getMonth(), calendarDate.getDayOfMonth());
            Text dateText = new Text(" " + String.valueOf(date.getDayOfMonth()));
            ListView<Appointment> appointmentsListView = new ListView<>();
            appointmentsListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent click) {
                    if (click.getClickCount() >= 2) {
                        Appointment appointment = appointmentsListView.getSelectionModel().getSelectedItem();
                        if (appointment == null) {
                            appointment = new Appointment();
                            LocalTime startTime = LocalTime.now().with(time -> {
                                int currentMinute = time.get(ChronoField.MINUTE_OF_DAY);
                                int interval = (currentMinute / 30) * 30 + 30;
                                time = time.with(ChronoField.SECOND_OF_MINUTE, 0);
                                time = time.with(ChronoField.MILLI_OF_SECOND, 0);
                                return time.with(ChronoField.MINUTE_OF_DAY, interval);
                            });
                            LocalTime endTime = LocalTime.now().with(time -> {
                                int currentMinute = time.get(ChronoField.MINUTE_OF_DAY);
                                int interval = (currentMinute / 30) * 30 + 60;
                                time = time.with(ChronoField.SECOND_OF_MINUTE, 0);
                                time = time.with(ChronoField.MILLI_OF_SECOND, 0);
                                return time.with(ChronoField.MINUTE_OF_DAY, interval);
                            });
                            appointment.setStart(java.sql.Timestamp.valueOf(LocalDateTime.of(date, startTime)));
                            appointment.setEnd(java.sql.Timestamp.valueOf(LocalDateTime.of(date, endTime)));
                        }
                        Stage primaryStage = (Stage) appointmentsListView.getScene().getWindow();
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("../appointment/AppointmentView.fxml"), resources);
                            final Stage createEditAppointmentStage = new Stage();
                            createEditAppointmentStage.initModality(Modality.APPLICATION_MODAL);
                            createEditAppointmentStage.initOwner(primaryStage);
                            Parent root = loader.load();
                            createEditAppointmentStage.setTitle("Create/Edit Appointment");
                            createEditAppointmentStage.setScene(new Scene(root, 560, 580));
                            createEditAppointmentStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                public void handle(WindowEvent we) {
                                    renderMonthlyCalendar();
                                }
                            });
                            createEditAppointmentStage.show();
                            AppointmentController controller = loader.<AppointmentController>getController();
                            controller.setAppointment(appointment);
                        } catch (IOException exception) {
                            return;
                        }
                    }
                }
            });
            appointmentsListView.setItems(appointments.stream()
                                                        .filter(appointment -> {
                                                            LocalDate startLocalDate = new java.sql.Date(appointment.getStart().getTime()).toLocalDate();
                                                            return date.equals(startLocalDate);
                                                        })
                                                        .collect(Collectors.toCollection(FXCollections::observableArrayList)));
            VBox.setVgrow(appointmentsListView, Priority.ALWAYS);
            VBox calendarDayContainer = new VBox(dateText, appointmentsListView);
            dayNode.setDate(date);
            dayNode.getChildren().add(calendarDayContainer);
            calendarDate = calendarDate.plusDays(1);
        }
    }

    @FXML
    private void navigateToPreviousMonth(ActionEvent actionEvent) {
        this.month = this.month.minusMonths(1);
        this.changeMonthText();
        this.renderMonthlyCalendar();
    }

    @FXML
    private void navigateToNextMonth(ActionEvent actionEvent) {
        this.month = this.month.plusMonths(1);
        this.changeMonthText();
        this.renderMonthlyCalendar();
    }

    @Override
    protected void reloadCalendar() {
        this.renderMonthlyCalendar();
    }
}
