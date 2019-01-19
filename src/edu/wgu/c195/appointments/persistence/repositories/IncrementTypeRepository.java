package edu.wgu.c195.appointments.persistence.repositories;

import edu.wgu.c195.appointments.domain.entities.IncrementType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IncrementTypeRepository extends RepositoryBase<IncrementType> {

    public IncrementTypeRepository(Connection connection) {
        super(connection);
    }

    @Override
    public IncrementType get(Object id) throws SQLException {
        String querySqlStr = "SELECT incrementTypeId, " +
                                    "incrementTypeDescription " +
                                "FROM incrementtypes " +
                                "WHERE incrementTypeId = ?";
        PreparedStatement queryStatement = super.connection.prepareStatement(querySqlStr);
        queryStatement.setInt(1, (int) id);
        ResultSet resultSet = queryStatement.executeQuery();
        IncrementType incrementType = null;
        if(resultSet.next()) {
            incrementType = new IncrementType();

            incrementType.setIncrementTypeId(resultSet.getInt("incrementTypeId"));
            incrementType.setIncrementTypeDescription(resultSet.getString("incrementTypeDescription"));
        }
        queryStatement.close();
        return incrementType;
    }

    @Override
    public void add(IncrementType entity) throws SQLException {
        String lastIdQueryStr = "SELECT incrementTypeId FROM incrementtypes ORDER BY incrementTypeId DESC LIMIT 1;";
        String insertSqlStr = "INSERT INTO incrementtypes (incrementTypeId, incrementTypeDescription) VALUES (?, ?);";

        super.startTransaction();
        PreparedStatement queryStatement = super.connection.prepareStatement(lastIdQueryStr);
        PreparedStatement insertStatement = super.connection.prepareStatement(insertSqlStr);
        ResultSet resultSet = queryStatement.executeQuery();
        if(resultSet.next()) {
            entity.setIncrementTypeId(resultSet.getInt("incrementTypeId") + 1);
        } else {
            entity.setIncrementTypeId(1);
        }
        insertStatement.setInt(1, entity.getIncrementTypeId());
        insertStatement.setString(2, entity.getIncrementTypeDescription());
        int recordsUpdated = insertStatement.executeUpdate();

        queryStatement.close();
        insertStatement.close();
    }

    @Override
    public void update(IncrementType entity) throws SQLException {
        String updateSqlStr = "UPDATE incrementtypes " +
                              "SET " +
                                "incrementTypeDescription = ? " +
                              "WHERE incrementTypeId = ?";
        super.startTransaction();
        PreparedStatement updateStatement = super.connection.prepareStatement(updateSqlStr);
        updateStatement.setString(1, entity.getIncrementTypeDescription());
        updateStatement.setInt(2, entity.getIncrementTypeId());
        int recordsUpdated = updateStatement.executeUpdate();
        updateStatement.close();
    }

    @Override
    public IncrementType delete(Object id) throws SQLException {
        IncrementType incrementType = get(id);
        if(incrementType != null) {
            String deleteSqlStr = "DELETE FROM incrementtypes WHERE incrementTypeId = ?";
            super.startTransaction();
            PreparedStatement deleteStatement = super.connection.prepareStatement(deleteSqlStr);
            deleteStatement.setInt(1, (int) id);
            int recordsUpdated = deleteStatement.executeUpdate();
            deleteStatement.close();
        }
        return incrementType;
    }
}
