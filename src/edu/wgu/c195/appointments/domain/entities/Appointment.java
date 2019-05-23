package edu.wgu.c195.appointments.domain.entities;

import edu.wgu.c195.appointments.domain.ValidationResult;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Appointment {
    private int appointmentId;
    private int customerId;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String url;
    private Timestamp start;
    private Timestamp end;
    private Date createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;

    public Appointment() {

    }

    public Appointment(int appointmentId, int customerId, String title, String description, String location, String contact, String url, Timestamp start, Timestamp end) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.url = url;
        this.start = start;
        this.end = end;
    }

    public Appointment(int appointmentId, int customerId, String title, String description, String location, String contact, String url, Timestamp start, Timestamp end, Date createDate, String createdBy) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.url = url;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createdBy = createdBy;
    }

    public Appointment(int appointmentId, int customerId, String title, String description, String location, String contact, String url, Timestamp start, Timestamp end, Date createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.url = url;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    @Override
    public String toString() {
        return this.getStart().toLocalDateTime().format(DateTimeFormatter.ofPattern("hh:mma"))
                + "-"
                + this.getEnd().toLocalDateTime().format(DateTimeFormatter.ofPattern("hh:mma"))
                + ": "
                + this.getTitle();
    }

    public ValidationResult validate() {
        List<String> errors = new ArrayList<>();
        if (this.customerId <= 0) {
            errors.add("You must select a customer.");
        }
        if (this.title == null || this.title.trim().equals("")) {
            errors.add("The title can't be empty.");
        }
        if (this.contact == null || this.contact.trim().equals("")) {
            errors.add("The contact can't be empty.");
        }
        if (this.url == null || this.url.trim().equals("")) {
            errors.add("You must select an appointment type.");
        }
        if (this.start.toLocalDateTime().isAfter(LocalDateTime.of(LocalDate.now(), LocalTime.of(19, 0)))) {
            errors.add("Your meetings start time is after 7:00pm which is past business hours.");
        }
        if (this.end.toLocalDateTime().isAfter(LocalDateTime.of(LocalDate.now(), LocalTime.of(19, 0)))) {
            errors.add("Your meetings end time is after 7:00pm which is past business hours.");
        }
        if (this.start.toLocalDateTime().isBefore(LocalDateTime.of(LocalDate.now(), LocalTime.of(7, 0)))) {
            errors.add("Your meetings start time is before 7:00am which is before business hours.");
        }
        if (this.end.toLocalDateTime().isBefore(LocalDateTime.of(LocalDate.now(), LocalTime.of(7, 0)))) {
            errors.add("Your meetings end time is before 7:00am which is before business hours.");
        }
        if (errors.size() > 0) {
            return new ValidationResult(errors);
        } else {
            return new ValidationResult();
        }
    }
}
