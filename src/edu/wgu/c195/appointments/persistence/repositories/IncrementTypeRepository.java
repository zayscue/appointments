package edu.wgu.c195.appointments.persistence.repositories;

import edu.wgu.c195.appointments.domain.Tuple;
import edu.wgu.c195.appointments.domain.entities.IncrementType;
import edu.wgu.c195.appointments.persistence.ConnectionFactory;
import edu.wgu.c195.appointments.persistence.SQL;

import java.sql.*;
import java.util.stream.Stream;

public class IncrementTypeRepository extends RepositoryBase<IncrementType> {

    public IncrementTypeRepository() {
        super(ConnectionFactory.getConnection());
    }

    public IncrementTypeRepository(Connection connection) {
        super(connection);
    }

    @Override
    public Stream<IncrementType> getAll() {
        return SQL.stream(super.connection, "SELECT incrementTypeId, incrementTypeDescription FROM incrementtypes")
                .map((t) -> {
                    IncrementType incrementType = new IncrementType();
                    incrementType.setIncrementTypeId(t.asInt("incrementTypeId"));
                    incrementType.setIncrementTypeDescription(t.asString("incrementTypeDescription"));
                    return incrementType;
                });
    }

    @Override
    public IncrementType get(Object id) throws SQLException {
        String querySqlStr = "SELECT incrementTypeId, " +
                                    "incrementTypeDescription " +
                                "FROM incrementtypes " +
                                "WHERE incrementTypeId = ?";
        PreparedStatement queryStatement = null;
        ResultSet resultSet = null;
        IncrementType incrementType = null;

        try {
            queryStatement = super.connection.prepareStatement(querySqlStr);
            queryStatement.setInt(1, (int) id);
            resultSet = queryStatement.executeQuery();
            if(resultSet.next()) {
                incrementType = new IncrementType();

                incrementType.setIncrementTypeId(resultSet.getInt("incrementTypeId"));
                incrementType.setIncrementTypeDescription(resultSet.getString("incrementTypeDescription"));
            }
        } catch(SQLException e) {
            throw e;
        } finally {
            if(queryStatement != null) {
                queryStatement.close();
            }
            if(resultSet != null) {
                resultSet.close();
            }
        }
        return incrementType;
    }

    @Override
    public void add(IncrementType entity) throws SQLException {
        String lastIdQueryStr = "SELECT incrementTypeId FROM incrementtypes ORDER BY incrementTypeId DESC LIMIT 1;";
        String insertSqlStr = "INSERT INTO incrementtypes (incrementTypeId, incrementTypeDescription) VALUES (?, ?);";
        PreparedStatement queryStatement = null;
        ResultSet resultSet = null;
        PreparedStatement insertStatement = null;

        try {
            super.startTransaction();
            queryStatement = super.connection.prepareStatement(lastIdQueryStr);
            insertStatement = super.connection.prepareStatement(insertSqlStr);
            resultSet = queryStatement.executeQuery();
            if(resultSet.next()) {
                entity.setIncrementTypeId(resultSet.getInt("incrementTypeId") + 1);
            } else {
                entity.setIncrementTypeId(1);
            }
            insertStatement.setInt(1, entity.getIncrementTypeId());
            insertStatement.setString(2, entity.getIncrementTypeDescription());
            int recordsUpdated = insertStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if(queryStatement != null) {
                queryStatement.close();
            }
            if(insertStatement != null) {
                insertStatement.close();
            }
            if(resultSet != null) {
                resultSet.close();
            }
        }
    }

    @Override
    public void update(IncrementType entity) throws SQLException {
        String updateSqlStr = "UPDATE incrementtypes " +
                              "SET " +
                                "incrementTypeDescription = ? " +
                              "WHERE incrementTypeId = ?";
        PreparedStatement updateStatement = null;

        try {
            super.startTransaction();
            updateStatement = super.connection.prepareStatement(updateSqlStr);
            updateStatement.setString(1, entity.getIncrementTypeDescription());
            updateStatement.setInt(2, entity.getIncrementTypeId());
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
    public IncrementType delete(Object id) throws SQLException {
        IncrementType incrementType = get(id);
        if(incrementType != null) {
            String deleteSqlStr = "DELETE FROM incrementtypes WHERE incrementTypeId = ?";
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
        return incrementType;
    }
}
