package edu.wgu.c195.appointments.domain.entities;

import java.sql.Date;

public class Reminder {
    private int reminderId;
    private Date reminderDate;
    private int snoozeIncrement;
    private int snoozeIncrementTypeId;
    private int appointmentId;
    private String createdBy;
    private Date createdDate;
    private String remindercol;

    public Reminder() {

    }

    public Reminder(int reminderId, Date reminderDate, int snoozeIncrement, int snoozeIncrementTypeId, int appointmentId) {
        this.reminderId = reminderId;
        this.reminderDate = reminderDate;
        this.snoozeIncrement = snoozeIncrement;
        this.snoozeIncrementTypeId = snoozeIncrementTypeId;
        this.appointmentId = appointmentId;
    }

    public Reminder(int reminderId, Date reminderDate, int snoozeIncrement, int snoozeIncrementTypeId, int appointmentId, String createdBy, Date createdDate) {
        this.reminderId = reminderId;
        this.reminderDate = reminderDate;
        this.snoozeIncrement = snoozeIncrement;
        this.snoozeIncrementTypeId = snoozeIncrementTypeId;
        this.appointmentId = appointmentId;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
    }

    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public Date getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }

    public int getSnoozeIncrement() {
        return snoozeIncrement;
    }

    public void setSnoozeIncrement(int snoozeIncrement) {
        this.snoozeIncrement = snoozeIncrement;
    }

    public int getSnoozeIncrementTypeId() {
        return snoozeIncrementTypeId;
    }

    public void setSnoozeIncrementTypeId(int snoozeIncrementTypeId) {
        this.snoozeIncrementTypeId = snoozeIncrementTypeId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getRemindercol() {
        return remindercol;
    }

    public void setRemindercol(String remindercol) {
        this.remindercol = remindercol;
    }
}
