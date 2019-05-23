package edu.wgu.c195.appointments.application;

import edu.wgu.c195.appointments.domain.entities.Appointment;
import edu.wgu.c195.appointments.persistence.ConnectionFactory;
import edu.wgu.c195.appointments.persistence.repositories.AppointmentRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReportsRunner {

    private final Connection connection;

    public ReportsRunner() {
        this.connection = ConnectionFactory.getConnection();
    }

    public List<AppointmentTypesByMonth> runAppointmentTypeByMonthReport() throws SQLException {
        String querySqlStr = "SELECT\n" +
                "    CONCAT(MONTH(start), '/' ,YEAR(start)) as 'Month',\n" +
                "    url as 'Type',\n" +
                "    COUNT(appointmentId) as 'Count'\n" +
                "FROM appointment\n" +
                "GROUP BY CONCAT(MONTH(start), '/' ,YEAR(start)), url";
        PreparedStatement queryStatement = null;
        ResultSet resultSet = null;
        List<AppointmentTypesByMonth> appointmentTypesByMonthList = new ArrayList<>();

        try {
            queryStatement = this.connection.prepareStatement(querySqlStr);
            resultSet = queryStatement.executeQuery();
            while (resultSet.next()) {
                AppointmentTypesByMonth appointmentTypesByMonthItem = new AppointmentTypesByMonth(
                        resultSet.getString("Month"),
                        resultSet.getString("Type"),
                        resultSet.getInt("Count")
                );
                appointmentTypesByMonthList.add(appointmentTypesByMonthItem);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if(queryStatement != null) {
                queryStatement.close();
            }
            if(resultSet != null) {
                resultSet.close();
            }
        }
        return appointmentTypesByMonthList;
    }

    public List<ConsultantSchedule> runConsultantScheduleReport() throws SQLException {
        String querySqlStr = "SELECT\n" +
                "    contact as 'Consultant'\n" +
                "FROM appointment\n" +
                "WHERE start > NOW()\n" +
                "GROUP BY contact";
        PreparedStatement queryStatement = null;
        ResultSet resultSet = null;
        List<ConsultantSchedule> consultantSchedules = new ArrayList<>();
        List<String> consultants = new ArrayList<>();

        try {
            queryStatement = this.connection.prepareStatement(querySqlStr);
            resultSet = queryStatement.executeQuery();
            while (resultSet.next()) {
                consultants.add(resultSet.getString("Consultant"));
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if(queryStatement != null) {
                queryStatement.close();
            }
            if(resultSet != null) {
                resultSet.close();
            }
        }


        AppointmentRepository repository = new AppointmentRepository(this.connection);
        for (String consultant : consultants) {
            List<Appointment> consultantAppointments = repository.getAll()
                    .filter(a -> a.getContact().equals(consultant) && a.getStart().after(Timestamp.valueOf(LocalDateTime.now())))
                    .collect(Collectors.toList());
            consultantSchedules.add(new ConsultantSchedule(consultant, consultantAppointments));
        }
        return consultantSchedules;
    }

    public List<AppointmentsPerCustomer> runNumberOfAppointmentsPerCustomerReport() throws SQLException {
        String querySqlStr = "SELECT\n" +
                "    customer.customerName,\n" +
                "    COUNT(appointment.appointmentId) as 'count'\n" +
                "FROM appointment\n" +
                "INNER JOIN customer\n" +
                "ON appointment.customerId = customer.customerId\n" +
                "GROUP BY customer.customerName";
        PreparedStatement queryStatement = null;
        ResultSet resultSet = null;
        List<AppointmentsPerCustomer> appointmentsPerCustomers = new ArrayList<>();

        try {
            queryStatement = this.connection.prepareStatement(querySqlStr);
            resultSet = queryStatement.executeQuery();
            while (resultSet.next()) {
                AppointmentsPerCustomer appointmentsPerCustomer = new AppointmentsPerCustomer(
                        resultSet.getString("customerName"),
                        resultSet.getInt("count")
                );
                appointmentsPerCustomers.add(appointmentsPerCustomer);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if(queryStatement != null) {
                queryStatement.close();
            }
            if(resultSet != null) {
                resultSet.close();
            }
        }

        return appointmentsPerCustomers;
    }
}
