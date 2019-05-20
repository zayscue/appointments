package edu.wgu.c195.appointments.application;

import edu.wgu.c195.appointments.domain.entities.Appointment;
import edu.wgu.c195.appointments.domain.entities.Customer;
import edu.wgu.c195.appointments.domain.entities.Reminder;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

public class AppointmentViewModel {

    private Appointment appointment;
    private List<Reminder> reminders;

    private StringProperty title = new SimpleStringProperty();
    private ObjectProperty<Customer> customer = new SimpleObjectProperty<>();
    private StringProperty location = new SimpleStringProperty();
    private StringProperty contact = new SimpleStringProperty();
    private StringProperty url = new SimpleStringProperty();
    private StringProperty description = new SimpleStringProperty();
    private ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>(LocalDate.now());
    private ObjectProperty<LocalTime> startTime = new SimpleObjectProperty<>(LocalTime.now().with(time -> {
        int currentMinute = time.get(ChronoField.MINUTE_OF_DAY);
        int interval = (currentMinute / 30) * 30 + 30;
        time = time.with(ChronoField.SECOND_OF_MINUTE, 0);
        time = time.with(ChronoField.MILLI_OF_SECOND, 0);
        return time.with(ChronoField.MINUTE_OF_DAY, interval);
    }));
    private ObjectProperty<LocalDate> endDate = new SimpleObjectProperty<>(LocalDate.now());
    private ObjectProperty<LocalTime> endTime = new SimpleObjectProperty<>(LocalTime.now().with(time -> {
        int currentMinute = time.get(ChronoField.MINUTE_OF_DAY);
        int interval = (currentMinute / 30) * 30 + 60;
        time = time.with(ChronoField.SECOND_OF_MINUTE, 0);
        time = time.with(ChronoField.MILLI_OF_SECOND, 0);
        return time.with(ChronoField.MINUTE_OF_DAY, interval);
    }));

    public AppointmentViewModel() {
        this.appointment = new Appointment();
        this.appointment.setStart(java.sql.Timestamp.valueOf(LocalDateTime.of(this.startDate.get(), this.startTime.get())));
        this.appointment.setEnd(java.sql.Timestamp.valueOf(LocalDateTime.of(this.endDate.get(), this.endTime.get())));
        this.reminders = new ArrayList<>();
        this.title.addListener((observable, oldValue, newValue) -> {
            this.appointment.setTitle(newValue);
        });
        this.customer.addListener((observable, oldValue, newValue) -> {
            this.appointment.setCustomerId(newValue.getCustomerId());
        });
        this.location.addListener((observable, oldValue, newValue) -> {
            this.appointment.setLocation(newValue);
        });
        this.contact.addListener((observable, oldValue, newValue) -> {
            this.appointment.setContact(newValue);
        });
        this.url.addListener((observable, oldValue, newValue) -> {
            this.appointment.setUrl(newValue);
        });
        this.description.addListener((observable, oldValue, newValue) -> {
            this.appointment.setDescription(newValue);
        });
        this.startDate.addListener((observable, oldValue, newValue) -> {
            this.appointment.setStart(java.sql.Timestamp.valueOf(LocalDateTime.of(newValue, this.startTime.get())));
        });
        this.startTime.addListener((observable, oldValue, newValue) -> {
            this.appointment.setStart(java.sql.Timestamp.valueOf(LocalDateTime.of(this.startDate.get(), newValue)));
        });
        this.endDate.addListener((observable, oldValue, newValue) -> {
            this.appointment.setEnd(java.sql.Timestamp.valueOf(LocalDateTime.of(newValue, this.endTime.get())));
        });
        this.endTime.addListener((observable, oldValue, newValue) -> {
            this.appointment.setEnd(java.sql.Timestamp.valueOf(LocalDateTime.of(this.endDate.get(), newValue)));
        });
    }

    public Appointment getAppointment() {
        return this.appointment;
    }

    public void setAppointment(Appointment appointment, Customer customer) {
        this.appointment = appointment;
        this.title.set(appointment.getTitle());
        this.customer.set(customer);
        this.location.set(appointment.getLocation());
        this.contact.set(appointment.getContact());
        this.url.set(appointment.getUrl());
        this.description.set(appointment.getDescription());
        this.startDate.set(appointment.getStart().toLocalDateTime().toLocalDate());
        this.startTime.set(appointment.getStart().toLocalDateTime().toLocalTime());
        this.endDate.set(appointment.getEnd().toLocalDateTime().toLocalDate());
        this.endTime.set(appointment.getEnd().toLocalDateTime().toLocalTime());
    }

    public StringProperty titleProperty() {
        return this.title;
    }

    public ObjectProperty<Customer> customerProperty() {
        return this.customer;
    }

    public StringProperty locationProperty() {
        return this.location;
    }

    public StringProperty contactProperty() {
        return this.contact;
    }

    public StringProperty urlProperty() {
        return this.url;
    }

    public StringProperty descriptionProperty() {
        return this.description;
    }

    public ObjectProperty<LocalDate> startDateProperty() {
        return this.startDate;
    }

    public ObjectProperty<LocalTime> startTimeProperty() {
        return this.startTime;
    }

    public ObjectProperty<LocalDate> endDateProperty() {
        return this.endDate;
    }

    public ObjectProperty<LocalTime> endTimeProperty() {
        return this.endTime;
    }
}
