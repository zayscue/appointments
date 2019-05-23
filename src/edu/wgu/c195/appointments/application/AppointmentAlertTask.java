package edu.wgu.c195.appointments.application;

import edu.wgu.c195.appointments.domain.entities.Appointment;
import edu.wgu.c195.appointments.persistence.repositories.AppointmentRepository;
import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentAlertTask implements Runnable {

    private final AppointmentRepository appointmentRepository;

    public AppointmentAlertTask() {
        this.appointmentRepository = new AppointmentRepository();
    }

    @Override
    public void run() {
        List<Appointment> alertableAppointments = this.appointmentRepository.getAll()
                                                                            .filter(appointment -> {
                                                                                LocalDateTime now = LocalDateTime.now();
                                                                                LocalDateTime alertEnd = now.plusMinutes(15);
                                                                                LocalDateTime start = appointment.getStart().toLocalDateTime();
                                                                                boolean checkOne = start.isAfter(now);
                                                                                boolean checkTwo = start.isBefore(alertEnd) || start.equals(alertEnd);
                                                                                return checkOne && checkTwo;
                                                                            })
                                                                            .collect(Collectors.toList());
        if (alertableAppointments.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Appointment alertableAppointment : alertableAppointments) {
                stringBuilder.append(alertableAppointment.toString() + "\n");
            }
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Upcoming Appointments");
            a.setHeaderText("Appointments within the next 15 minutes: ");
            a.setContentText(stringBuilder.toString());
            a.showAndWait();
        }
    }
}
