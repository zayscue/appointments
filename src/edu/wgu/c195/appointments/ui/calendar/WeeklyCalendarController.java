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
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class WeeklyCalendarController extends CalendarView implements Initializable {

    private final AppointmentRepository appointmentRepository;
    private final List<CalendarDayNode> dayNodes = new ArrayList<>(7);

    @FXML
    private Button previousWeekBtn;
    @FXML
    private Text weekText;
    @FXML
    private Button nextWeekBtn;
    @FXML
    private GridPane weeklyCalendarHeader;
    @FXML
    private GridPane weeklyCalendar;

    private ResourceBundle resources;
    private LocalDate firstDay;

    public WeeklyCalendarController(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
        YearMonth month = YearMonth.now();
        LocalDate firstDayOfTheMonth = LocalDate.of(month.getYear(), month.getMonthValue(), 1);
        while (firstDayOfTheMonth.getDayOfWeek() != DayOfWeek.SUNDAY) {
            firstDayOfTheMonth = firstDayOfTheMonth.minusDays(1);
        }
        this.firstDay = firstDayOfTheMonth;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        this.previousWeekBtn.setText("<<");
        this.previousWeekBtn.setOnAction(this::navigateToPreviousWeek);
        this.nextWeekBtn.setText(">>");
        this.nextWeekBtn.setOnAction(this::navigateToNextWeek);
        this.changeWeekText();
        this.initializeWeeklyCalendarHeader();
        this.initializeWeeklyCalendar();
    }

    private void changeWeekText() {
        this.weekText.setText(" Week of: "
                + this.firstDay.getMonthValue()
                + "/"
                + this.firstDay.getDayOfMonth()
                + " - "
                + this.firstDay.plusDays(7).getMonthValue()
                + "/" + this.firstDay.plusDays(7).getDayOfMonth()
                + " "
        );
    }

    private void initializeWeeklyCalendarHeader() {
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
            StackPane sp = new StackPane();
            sp.setPrefHeight(10);
            GridPane.setHgrow(sp, Priority.ALWAYS);
            sp.getChildren().add(dayName);
            this.weeklyCalendarHeader.add(sp, column++, 0);
        }
    }

    private void initializeWeeklyCalendar() {
        for (int i = 0; i < 7; i++) {
            CalendarDayNode ap = new CalendarDayNode();
            this.weeklyCalendar.add(ap,i,0);
            GridPane.setHgrow(ap, Priority.ALWAYS);
            GridPane.setVgrow(ap, Priority.ALWAYS);
            VBox.setVgrow(ap, Priority.ALWAYS);
            this.dayNodes.add(ap);
        }
        this.renderWeeklyCalendar();
    }

    private void renderWeeklyCalendar() {
        List<Appointment> appointments = this.appointmentRepository.getAll()
                .filter(a -> a.getStart().after(Date.valueOf(this.firstDay))
                    && a.getStart().before(Date.valueOf(this.firstDay.plusDays(7)))
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
        LocalDate calendarDate = LocalDate.of(this.firstDay.getYear(), this.firstDay.getMonthValue(), this.firstDay.getDayOfMonth());
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
                                    renderWeeklyCalendar();
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
    private void navigateToPreviousWeek(ActionEvent actionEvent) {
        this.firstDay = this.firstDay.minusDays(7);
        this.changeWeekText();
        this.renderWeeklyCalendar();
    }

    @FXML
    private void navigateToNextWeek(ActionEvent actionEvent) {
        this.firstDay = this.firstDay.plusDays(7);
        this.changeWeekText();
        this.renderWeeklyCalendar();
    }

    @Override
    protected void reloadCalendar() {
        this.renderWeeklyCalendar();
    }
}
