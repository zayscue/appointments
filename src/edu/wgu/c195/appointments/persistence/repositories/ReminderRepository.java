package edu.wgu.c195.appointments.persistence.repositories;

import edu.wgu.c195.appointments.domain.entities.Reminder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReminderRepository extends RepositoryBase<Reminder> {

    public ReminderRepository(Connection connection) {
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
        queryStatement.close();
        return reminder;
    }

    @Override
    public void add(Reminder entity) throws SQLException {
        String lastIdSqlQueryStr = "SELECT reminderId FROM reminder ORDER BY reminderId DESC LIMIT 1";
        String insertSqlStr = "INSERT INTO reminder (" +
                                            "reminderId, " +
                                            "reminderDate, " +
                                            "snoozeIncrement, " +
                                            "snoozeIncrementTypeId, " +
                                            "appointmentId, " +
                                            "createdBy, " +
                                            "createdDate, " +
                                            "remindercol) " +
                                    "VALUES (" +
                                            "?, " +
                                            "?, " +
                                            "?, " +
                                            "?, " +
                                            "?, " +
                                            "?, " +
                                            "?, " +
                                            "?)";

        super.startTransaction();
        PreparedStatement queryStatement = super.connection.prepareStatement(lastIdSqlQueryStr);
        PreparedStatement insertStatement = super.connection.prepareStatement(insertSqlStr);
        ResultSet resultSet = queryStatement.executeQuery();
        if(resultSet.next()) {
            entity.setReminderId(resultSet.getInt("reminderId") + 1);
        } else {
            entity.setReminderId(1);
        }
        insertStatement.setInt(1, entity.getReminderId());
        insertStatement.setDate(2, entity.getReminderDate());
        insertStatement.setInt(3, entity.getSnoozeIncrement());
        insertStatement.setInt(4, entity.getSnoozeIncrementTypeId());
        insertStatement.setInt(5, entity.getAppointmentId());
        insertStatement.setString(6, entity.getCreatedBy());
        insertStatement.setDate(7, entity.getCreatedDate());
        insertStatement.setString(8, entity.getRemindercol());
        int recordsUpdated = insertStatement.executeUpdate();

        queryStatement.close();
        insertStatement.close();
    }

    @Override
    public void update(Reminder entity) throws SQLException {
        String updateSqlStr = "UPDATE reminder " +
                              "SET " +
                                "reminderDate = ?, " +
                                "snoozeIncrement = ?, " +
                                "snoozeIncrementTypeId = ?, " +
                                "remindercol = ? " +
                              "WHERE reminderId = ?";
        super.startTransaction();
        PreparedStatement updateStatement = super.connection.prepareStatement(updateSqlStr);
        updateStatement.setDate(1, entity.getReminderDate());
        updateStatement.setInt(2, entity.getSnoozeIncrement());
        updateStatement.setInt(3, entity.getSnoozeIncrementTypeId());
        updateStatement.setString(4, entity.getRemindercol());
        updateStatement.setInt(5, entity.getReminderId());
        int recordsUpdated = updateStatement.executeUpdate();
        updateStatement.close();
    }

    @Override
    public Reminder delete(Object id) throws SQLException {
        Reminder reminder = this.get(id);
        if(reminder != null) {
            String deleteSqlStr = "DELETE FROM reminder WHERE reminderId = ?";
            super.startTransaction();
            PreparedStatement deleteStatement = super.connection.prepareStatement(deleteSqlStr);
            deleteStatement.setInt(1, (int) id);
            int recordsUpdated = deleteStatement.executeUpdate();
            deleteStatement.close();
        }
        return reminder;
    }
}
