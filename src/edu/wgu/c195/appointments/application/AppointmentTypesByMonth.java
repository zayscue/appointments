package edu.wgu.c195.appointments.application;

public class AppointmentTypesByMonth {
    private final String month;
    private final String appointmentType;

    public String getMonth() {
        return month;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public int getCount() {
        return count;
    }

    private final int count;


    public AppointmentTypesByMonth(String month, String appointmentType, int count) {
        this.month = month;
        this.appointmentType = appointmentType;
        this.count = count;
    }
}
