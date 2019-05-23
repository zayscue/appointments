package edu.wgu.c195.appointments.ui.appointment;

import edu.wgu.c195.appointments.application.AppointmentViewModel;
import edu.wgu.c195.appointments.domain.ValidationResult;
import edu.wgu.c195.appointments.domain.entities.Appointment;
import edu.wgu.c195.appointments.domain.entities.Customer;
import edu.wgu.c195.appointments.domain.exceptions.ConflictingAppointmentException;
import edu.wgu.c195.appointments.domain.exceptions.InvalidAppointmentDataException;
import edu.wgu.c195.appointments.persistence.SQL;
import edu.wgu.c195.appointments.persistence.repositories.AppointmentRepository;
import edu.wgu.c195.appointments.persistence.repositories.CustomerRepository;
import edu.wgu.c195.appointments.ui.AppointmentsUI;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.converter.LocalTimeStringConverter;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST;

public class AppointmentController implements Initializable {

    private final AppointmentRepository appointments;
    private final CustomerRepository customerRepository;
    private final ObservableList<Customer> customers;
    private final ObservableList<String> appointmentTypes = FXCollections.observableArrayList("Dinner Meeting",
                                                                                                            "Lunch Meeting",
                                                                                                            "Sports Game",
                                                                                                            "Theater Show");

    @FXML
    private TextField titleTextField;
    @FXML
    private ComboBox<Customer> customersComboBox;
    @FXML
    private TextField locationTextField;
    @FXML
    private TextField contactTextField;
    @FXML
    private ComboBox typeComboBox;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private TextField startTimeTextField;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField endTimeTextField;
    @FXML
    private Text errorMessageText;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;
    private ResourceBundle resources;
    private AppointmentViewModel viewModel;

    public AppointmentController() {
        viewModel = new AppointmentViewModel();
        this.appointments = new AppointmentRepository();
        this.customerRepository = new CustomerRepository();
        // Building on my object type stream from the database
        // I used a filter and a collect to map the results to an observableArrayList
        // so they could be bound to a combo box.
        this.customers = this.customerRepository.getAll()
                .filter(customer -> customer.isActive())
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;

        this.customersComboBox.setItems(this.customers);
        this.typeComboBox.setItems(this.appointmentTypes);

        Bindings.bindBidirectional(this.titleTextField.textProperty(), this.viewModel.titleProperty());
        Bindings.bindBidirectional(this.customersComboBox.valueProperty(), this.viewModel.customerProperty());
        Bindings.bindBidirectional(this.locationTextField.textProperty(), this.viewModel.locationProperty());
        Bindings.bindBidirectional(this.contactTextField.textProperty(), this.viewModel.contactProperty());
        Bindings.bindBidirectional(this.typeComboBox.valueProperty(), this.viewModel.urlProperty());
        Bindings.bindBidirectional(this.descriptionTextArea.textProperty(), this.viewModel.descriptionProperty());
        Bindings.bindBidirectional(this.startDatePicker.valueProperty(), this.viewModel.startDateProperty());
        Bindings.bindBidirectional(this.startTimeTextField.textProperty(), this.viewModel.startTimeProperty(), new LocalTimeStringConverter());
        Bindings.bindBidirectional(this.endDatePicker.valueProperty(), this.viewModel.endDateProperty());
        Bindings.bindBidirectional(this.endTimeTextField.textProperty(), this.viewModel.endTimeProperty(), new LocalTimeStringConverter());
    }

    public void setAppointment(Appointment appointment) {
        this.viewModel.setAppointment(appointment);
        if (this.viewModel.getAppointment().getAppointmentId() > 0) {
            this.deleteBtn.setVisible(true);
        }
        if (appointment.getCustomerId() > 0) {
            // Query customer by id from the observable list of customers
            Optional<Customer> customer = this.customers
                    .stream()
                    .filter(x -> x.getCustomerId() == appointment.getCustomerId())
                    .findFirst();
            if (customer.isPresent()) {
                this.viewModel.setCustomer(customer.get());
            } else {
                try {
                    Customer inactiveCustomer = this.customerRepository.get(appointment.getCustomerId());
                    if (inactiveCustomer != null) {
                        this.viewModel.setCustomer(inactiveCustomer);
                    } else {
                        this.viewModel.setCustomer(null);
                    }
                } catch (SQLException e) {
                    this.viewModel.setCustomer(null);
                }
            }
        }
    }

    @FXML
    private void onSaveBtnClicked(ActionEvent event) throws SQLException {
        Date currentDate = java.sql.Date.valueOf(LocalDate.now());
        Timestamp currentTimestamp = java.sql.Timestamp.valueOf(LocalDateTime.now());
        String currentUser = AppointmentsUI.CurrentUser.getUserName();

        Appointment appointment = this.viewModel.getAppointment();
        try {
            if (appointment == null) {
                throw new InvalidAppointmentDataException();
            }
            ValidationResult validationResult = appointment.validate();
            if (!validationResult.isValid()) {
                throw new InvalidAppointmentDataException(validationResult);
            }
            boolean conflictingAppointments = this.appointments.getAll()
                    .anyMatch(a -> {
                        boolean startTimeConflict = appointment.getStart().equals(a.getStart())
                                || appointment.getStart().after(a.getStart())
                                && appointment.getStart().before(a.getEnd())
                                || appointment.getStart().equals(a.getEnd());
                        boolean endTimeConflict = appointment.getEnd().equals(a.getEnd())
                                || appointment.getEnd().before(a.getEnd())
                                && appointment.getEnd().after(a.getStart())
                                || appointment.getEnd().equals(a.getStart());
                        return startTimeConflict || endTimeConflict;
                    });
            if (conflictingAppointments) {
                throw new ConflictingAppointmentException();
            }
            appointment.setLastUpdate(currentTimestamp);
            appointment.setLastUpdateBy(currentUser);
            if (appointment.getAppointmentId() > 0) {
                this.appointments.update(appointment);
            } else {
                appointment.setCreateDate(currentDate);
                appointment.setCreatedBy(currentUser);
                this.appointments.add(appointment);
            }
            this.appointments.save();

            Stage createEditAppointmentStage = (Stage) this.saveBtn.getScene().getWindow();
            createEditAppointmentStage.getOnCloseRequest().handle(new WindowEvent(createEditAppointmentStage, WINDOW_CLOSE_REQUEST));
            createEditAppointmentStage.close();
        } catch (InvalidAppointmentDataException invalidAppointment) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Ooops, there was an error!");
            alert.setContentText(invalidAppointment.getMessage());
            alert.showAndWait();
        } catch (ConflictingAppointmentException conflictingAppointment) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Ooops, there was an error!");
            alert.setContentText(conflictingAppointment.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void onCancelBtnClicked(ActionEvent event) {
        Stage createEditAppointmentStage = (Stage) this.cancelBtn.getScene().getWindow();
        createEditAppointmentStage.getOnCloseRequest().handle(new WindowEvent(createEditAppointmentStage, WINDOW_CLOSE_REQUEST));
        createEditAppointmentStage.close();
    }

    @FXML
    private void onDeleteBtnClicked(ActionEvent actionEvent) {
        try {
            Appointment appointment = this.viewModel.getAppointment();
            this.appointments.delete(appointment.getAppointmentId());
            this.appointments.save();
            Stage createEditAppointmentStage = (Stage) this.cancelBtn.getScene().getWindow();
            createEditAppointmentStage.getOnCloseRequest().handle(new WindowEvent(createEditAppointmentStage, WINDOW_CLOSE_REQUEST));
            createEditAppointmentStage.close();
        } catch (SQLException e) {
            this.errorMessageText.setText("There an issue occurred while trying to delete this appointment from the database.");
        }
    }
}
