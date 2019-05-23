package edu.wgu.c195.appointments.application;

import edu.wgu.c195.appointments.domain.entities.Appointment;

import java.util.List;

public class ConsultantSchedule {
    private final String consultant;

    public String getConsultant() {
        return consultant;
    }

    public List<Appointment> getUpcomingAppointments() {
        return upcomingAppointments;
    }

    private final List<Appointment> upcomingAppointments;

    public ConsultantSchedule(String consultant, List<Appointment> upcomingAppointments) {
        this.consultant = consultant;
        this.upcomingAppointments = upcomingAppointments;
    }
}
