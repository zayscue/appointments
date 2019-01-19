package edu.wgu.c195.appointments.persistence.repositories;

import edu.wgu.c195.appointments.domain.entities.Reminder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReminderRepository extends RepositoryBase<Reminder> {

    protected ReminderRepository(Connection connection) {
        super(connection);
    }

    @Override
    public Reminder get(Object id) throws SQLException {
        String querySqlStr = "SELECT reminderId, " +
                                    "reminderDate, " +
                                    "snoozeIncrement, " +
                                    "snoozeIncrementTypeId, " +
                                    "appointmentId, " +
                                    "createdBy, " +
                                    "createdDate, " +
                                    "remindercol " +
                                "FROM reminder " +
                                "WHERE reminderId = ?";
        PreparedStatement queryStatement = super.connection.prepareStatement(querySqlStr);
        queryStatement.setInt(1, (int) id);
        ResultSet resultSet = queryStatement.executeQuery();
        Reminder reminder = null;
        if(resultSet.next()) {
            reminder = new Reminder();

            reminder.setReminderId(resultSet.getInt("reminderId"));
            reminder.setReminderDate(resultSet.getDate("reminderDate"));
            reminder.setSnoozeIncrement(resultSet.getInt("snoozeIncrement"));
            reminder.setSnoozeIncrementTypeId(resultSet.getInt("snoozeIncrementTypeId"));
            reminder.setAppointmentId(resultSet.getInt("appointmentId"));
            reminder.setCreatedBy(resultSet.getString("createdBy"));
            reminder.setCreatedDate(resultSet.getDate("createdDate"));
            reminder.setRemindercol(resultSet.getString("remindercol"));
        }
        return reminder;
    }

    @Override
    public void add(Reminder entity) throws SQLException {

    }

    @Override
    public void update(Reminder entity) throws SQLException {

    }

    @Override
    public Reminder delete(Object id) throws SQLException {
        return null;
    }
}
