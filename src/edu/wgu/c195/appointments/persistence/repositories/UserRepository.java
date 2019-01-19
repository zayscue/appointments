package edu.wgu.c195.appointments.persistence.repositories;

import edu.wgu.c195.appointments.domain.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository extends RepositoryBase<User> {

    public UserRepository(Connection connection) {
        super(connection);
    }

    @Override
    public User get(Object id) throws SQLException {
        String queryString = "SELECT userId, " +
                                    "userName, " +
                                    "password, " +
                                    "active, " +
                                    "createBy, " +
                                    "createDate, " +
                                    "lastUpdate, " +
                                    "lastUpdatedBy " +
                                "FROM user " +
                                "WHERE userId = ?";
        PreparedStatement queryStatement = super.connection.prepareStatement(queryString);
        queryStatement.setInt(1, (int) id);
        ResultSet resultSet = queryStatement.executeQuery();
        User user = null;
        if(resultSet.next()) {
            user = new User();

            user.setUserId(resultSet.getInt("userId"));
            user.setUserName(resultSet.getString("userName"));
            user.setPassword(resultSet.getString("password"));
            user.setActive(resultSet.getByte("active"));
            user.setCreateBy(resultSet.getString("createBy"));
            user.setCreateDate(resultSet.getDate("createDate"));
            user.setLastUpdate(resultSet.getTimestamp("lastUpdate"));
            user.setLastUpdatedBy(resultSet.getString("lastUpdatedBy"));
        }
        queryStatement.close();
        return user;
    }

    @Override
    public void add(User entity) throws SQLException {
        String lastIdQueryStr = "SELECT userId FROM user ORDER BY userId DESC LIMIT 1";
        String insertSqlStr = "INSERT INTO user (userId, userName, password, active, createBy, createDate, lastUpdate, lastUpdatedBy) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        super.startTransaction();
        PreparedStatement queryStatement = super.connection.prepareStatement(lastIdQueryStr);
        PreparedStatement insertStatement = super.connection.prepareStatement(insertSqlStr);
        ResultSet resultSet = queryStatement.executeQuery();
        if(resultSet.next()) {
            entity.setUserId(resultSet.getInt("userId") + 1);
        } else {
            entity.setUserId(1);
        }
        insertStatement.setInt(1, entity.getUserId());
        insertStatement.setString(2, entity.getUserName());
        insertStatement.setString(3, entity.getPassword());
        insertStatement.setByte(4, entity.getActive());
        insertStatement.setString(5, entity.getCreateBy());
        insertStatement.setDate(6, entity.getCreateDate());
        insertStatement.setTimestamp(7, entity.getLastUpdate());
        insertStatement.setString(8, entity.getLastUpdatedBy());
        int recordsUpdated = insertStatement.executeUpdate();

        queryStatement.close();
        insertStatement.close();
    }

    @Override
    public void update(User entity) throws SQLException {
        String updateSqlStr = "UPDATE user " +
                              "SET " +
                                "userName = ?, " +
                                "password = ?, " +
                                "active = ?, " +
                                "lastUpdate = ?, " +
                                "lastUpdatedBy = ?" +
                              "WHERE userId = ? ";
        super.startTransaction();
        PreparedStatement updateStatement = super.connection.prepareStatement(updateSqlStr);
        updateStatement.setString(1, entity.getUserName());
        updateStatement.setString(2, entity.getPassword());
        updateStatement.setByte(3, entity.getActive());
        updateStatement.setTimestamp(4, entity.getLastUpdate());
        updateStatement.setString(5, entity.getLastUpdatedBy());
        updateStatement.setInt(6, entity.getUserId());
        int recordsUpdated = updateStatement.executeUpdate();
        updateStatement.close();
    }

    @Override
    public User delete(Object id) throws SQLException {
        User user = get(id);
        if(user != null) {
            String deleteSqlStr = "DELETE FROM user WHERE userId = ? ";
            super.startTransaction();
            PreparedStatement deleteStatement = super.connection.prepareStatement(deleteSqlStr);
            deleteStatement.setInt(1, (int) id);
            int recordsUpdated = deleteStatement.executeUpdate();
            deleteStatement.close();
        }
        return user;
    }
}
