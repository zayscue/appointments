package edu.wgu.c195.appointments.ui.calendar;

import edu.wgu.c195.appointments.application.AppointmentTypesByMonth;
import edu.wgu.c195.appointments.application.AppointmentsPerCustomer;
import edu.wgu.c195.appointments.application.ConsultantSchedule;
import edu.wgu.c195.appointments.application.ReportsRunner;
import edu.wgu.c195.appointments.domain.entities.Appointment;
import edu.wgu.c195.appointments.persistence.repositories.AppointmentRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    private final ReportsRunner reportsRunner;

    public MainController() {
        this.appointmentRepository = new AppointmentRepository();
        this.reportsRunner = new ReportsRunner();
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
        createEditAppointmentStage.setScene(new Scene(root, 640, 700));
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

    @FXML
    private void runAppointmentTypeByMonthReport(ActionEvent actionEvent) throws SQLException {
        List<AppointmentTypesByMonth> results = this.reportsRunner.runAppointmentTypeByMonthReport();
        List<String> appointmentTypes = results.stream()
                .map(a -> a.getAppointmentType())
                .distinct()
                .collect(Collectors.toList());
        Stage primaryStage = (Stage) this.newAppointmentBtn.getScene().getWindow();
        final Stage reportStage  = new Stage();
        reportStage.initModality(Modality.APPLICATION_MODAL);
        reportStage.initOwner(primaryStage);
        reportStage.setTitle("Report");
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String, Number> bc = new BarChart<String, Number>(xAxis, yAxis);
        bc.setTitle("Number Of Appointment Types By Month");
        xAxis.setLabel("Month");
        yAxis.setLabel("Count");

        for (String appointmentType : appointmentTypes) {
            XYChart.Series series = new XYChart.Series();
            series.setName(appointmentType);
            List<AppointmentTypesByMonth> appointmentTypesThisMonth = results.stream()
                    .filter(a -> a.getAppointmentType().equals(appointmentType))
                    .collect(Collectors.toList());
            for (AppointmentTypesByMonth appointmentTypesByMonth : appointmentTypesThisMonth) {
                series.getData().add(new XYChart.Data(appointmentTypesByMonth.getMonth(), appointmentTypesByMonth.getCount()));
            }
            bc.getData().add(series);
        }

        Scene scene = new Scene(bc, 800, 600);
        reportStage.setScene(scene);
        reportStage.show();
    }

    @FXML
    private void runScheduleForEachConsultantReport(ActionEvent actionEvent) throws SQLException {
        List<ConsultantSchedule> consultantSchedules = this.reportsRunner.runConsultantScheduleReport();
        Stage primaryStage = (Stage) this.newAppointmentBtn.getScene().getWindow();
        final Stage reportStage  = new Stage();
        reportStage.initModality(Modality.APPLICATION_MODAL);
        reportStage.initOwner(primaryStage);
        reportStage.setTitle("Report");
        GridPane consultantsNode = new GridPane();
        consultantsNode.setPadding(new Insets(10));
        Text title = new Text("Consultants Schedules");
        title.setTextAlignment(TextAlignment.CENTER);
        consultantsNode.add(title, 0, 0);
        int count = 1;
        for (ConsultantSchedule schedule : consultantSchedules) {
            Text text = new Text(schedule.getConsultant());
            ListView listView = new ListView();
            listView.setPrefWidth(580);
            listView.setItems(schedule.getUpcomingAppointments()
                    .stream()
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
                    .map(a -> a.getStart().toLocalDateTime().format(DateTimeFormatter.ofPattern("MM-yyyy")) + " " + a.toString())
                    .collect(Collectors.toCollection(FXCollections::observableArrayList))
            );
            VBox consultantNode = new VBox(text, listView);
            GridPane.setHgrow(consultantNode, Priority.ALWAYS);
            GridPane.setVgrow(consultantNode, Priority.ALWAYS);
            consultantsNode.add(consultantNode, 0, count);
            count++;
        }
        ScrollPane scrollPane = new ScrollPane(consultantsNode);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        Scene scene = new Scene(scrollPane, 800, 600);
        reportStage.setScene(scene);
        reportStage.show();
    }

    @FXML
    private void runNumberOfAppointmentsPerCustomerReport(ActionEvent actionEvent) throws SQLException {
        List<AppointmentsPerCustomer> appointmentsPerCustomers = this.reportsRunner.runNumberOfAppointmentsPerCustomerReport();
        Stage primaryStage = (Stage) this.newAppointmentBtn.getScene().getWindow();
        final Stage reportStage  = new Stage();
        reportStage.initModality(Modality.APPLICATION_MODAL);
        reportStage.initOwner(primaryStage);
        reportStage.setTitle("Report");

        ObservableList<PieChart.Data> pieChartData = appointmentsPerCustomers
                .stream()
                .map(a -> new PieChart.Data(a.getCustomerName(), a.getCount()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Number of Appointment Per Customer");

        Scene scene = new Scene(chart, 800, 600);
        reportStage.setScene(scene);
        reportStage.show();
    }
}
