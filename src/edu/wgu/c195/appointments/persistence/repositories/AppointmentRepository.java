package edu.wgu.c195.appointments.persistence.repositories;

import edu.wgu.c195.appointments.domain.entities.Appointment;
import edu.wgu.c195.appointments.persistence.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AppointmentRepository extends RepositoryBase<Appointment> {

    public AppointmentRepository() {
        super(ConnectionFactory.getConnection());
    }

    public AppointmentRepository(Connection connection) {
        super(connection);
    }

    @Override
    public Appointment get(Object id) throws SQLException {
        String querySqlStr = "SELECT appointmentId, " +
                                    "customerId, " +
                                    "title, " +
                                    "description, " +
                                    "location, " +
                                    "contact, " +
                                    "url, " +
                                    "start, " +
                                    "end, " +
                                    "createDate, " +
                                    "createdBy, " +
                                    "lastUpdate, " +
                                    "lastUpdateBy " +
                            "FROM appointment " +
                            "WHERE appointmentId = ?;";

        PreparedStatement queryStatement = super.connection.prepareStatement(querySqlStr);
        queryStatement.setInt(1, (int) id);
        ResultSet resultSet = queryStatement.executeQuery();
        Appointment appointment = null;
        if(resultSet.next()) {
            appointment = new Appointment();

            appointment.setAppointmentId(resultSet.getInt("appointmentId"));
            appointment.setCustomerId(resultSet.getInt("customerId"));
            appointment.setTitle(resultSet.getString("title"));
            appointment.setDescription(resultSet.getString("description"));
            appointment.setLocation(resultSet.getString("location"));
            appointment.setContact(resultSet.getString("contact"));
            appointment.setUrl(resultSet.getString("url"));
            appointment.setStart(resultSet.getDate("start"));
            appointment.setEnd(resultSet.getDate("end"));
            appointment.setCreateDate(resultSet.getDate("createDate"));
            appointment.setCreatedBy(resultSet.getString("createdBy"));
            appointment.setLastUpdate(resultSet.getTimestamp("lastUpdate"));
            appointment.setLastUpdateBy(resultSet.getString("lastUpdateBy"));
        }
        queryStatement.close();

        return appointment;
    }

    @Override
    public void add(Appointment entity) throws SQLException {
        String lastIdQuerySqlStr = "SELECT appointmentId FROM appointment ORDER BY appointmentId DESC LIMIT 1;";
        String insertSqlStr = "INSERT INTO appointment (appointmentId, " +
                                                        "customerId, " +
                                                        "title, " +
                                                        "description, " +
                                                        "location, " +
                                                        "contact, " +
                                                        "url, " +
                                                        "start, " +
                                                        "end, " +
                                                        "createDate, " +
                                                        "createdBy, " +
                                                        "lastUpdate, " +
                                                        "lastUpdateBy) " +
                                                "VALUES (?, " +
                                                        "?, " +
                                                        "?, " +
                                                        "?, " +
                                                        "?, " +
                                                        "?, " +
                                                        "?, " +
                                                        "?, " +
                                                        "?, " +
                                                        "?, " +
                                                        "?, " +
                                                        "?, " +
                                                        "?);";
        PreparedStatement queryStatement = null;
        PreparedStatement insertStatement = null;

        try {
            super.startTransaction();
            queryStatement = super.connection.prepareStatement(lastIdQuerySqlStr);
            insertStatement = super.connection.prepareStatement(insertSqlStr);
            ResultSet resultSet = queryStatement.executeQuery();
            if(resultSet.next()) {
                entity.setAppointmentId(resultSet.getInt("appointmentId") + 1);
            } else {
                entity.setAppointmentId(1);
            }
            insertStatement.setInt(1, entity.getAppointmentId());
            insertStatement.setInt(2, entity.getCustomerId());
            insertStatement.setString(3, entity.getTitle());
            insertStatement.setString(4, entity.getDescription());
            insertStatement.setString(5, entity.getLocation());
            insertStatement.setString(6, entity.getContact());
            insertStatement.setString(7, entity.getUrl());
            insertStatement.setDate(8, entity.getStart());
            insertStatement.setDate(9, entity.getEnd());
            insertStatement.setDate(10, entity.getCreateDate());
            insertStatement.setString(11, entity.getCreatedBy());
            insertStatement.setTimestamp(12, entity.getLastUpdate());
            insertStatement.setString(13, entity.getLastUpdateBy());
            int updatedRecords = insertStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if(queryStatement != null) {
                queryStatement.close();
            }
            if(insertStatement != null) {
                insertStatement.close();
            }
        }
    }

    @Override
    public void update(Appointment entity) throws SQLException {
        String updateSqlStr = "UPDATE appointment " +
                                "SET customerId = ?, " +
                                    "title = ?, " +
                                    "description = ?, " +
                                    "location = ?, " +
                                    "contact = ?, " +
                                    "url = ?, " +
                                    "start = ?, " +
                                    "end = ?, " +
                                    "lastUpdate = ?, " +
                                    "lastUpdateBy = ? " +
                                "WHERE appointmentId = ?;";
        PreparedStatement updateStatement = null;

        try {
            super.startTransaction();
            updateStatement = super.connection.prepareStatement(updateSqlStr);
            updateStatement.setInt(1, entity.getCustomerId());
            updateStatement.setString(2, entity.getTitle());
            updateStatement.setString(3, entity.getDescription());
            updateStatement.setString(4, entity.getLocation());
            updateStatement.setString(5, entity.getContact());
            updateStatement.setString(6, entity.getUrl());
            updateStatement.setDate(7, entity.getStart());
            updateStatement.setDate(8, entity.getEnd());
            updateStatement.setTimestamp(9, entity.getLastUpdate());
            updateStatement.setString(10, entity.getLastUpdateBy());
            updateStatement.setInt(11, entity.getAppointmentId());
            int recordsUpdated = updateStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if(updateStatement != null) {
                updateStatement.close();
            }
        }
    }

    @Override
    public Appointment delete(Object id) throws SQLException {
        Appointment appointment = this.get(id);
        if(appointment != null) {
            String deleteSqlStr = "DELETE FROM appointment WHERE appointmentId = ?;";
            PreparedStatement deleteStatement = null;
            try {
                super.startTransaction();
                deleteStatement = super.connection.prepareStatement(deleteSqlStr);
                deleteStatement.setInt(1, (int) id);
                int recordsUpdated = deleteStatement.executeUpdate();
            } catch (SQLException e) {
                throw e;
            } finally {
                if(deleteStatement != null) {
                    deleteStatement.close();
                }
            }
        }
        return appointment;
    }
}
