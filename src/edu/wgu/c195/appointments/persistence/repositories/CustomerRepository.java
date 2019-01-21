package edu.wgu.c195.appointments.persistence.repositories;

import edu.wgu.c195.appointments.domain.entities.Customer;
import edu.wgu.c195.appointments.persistence.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRepository extends RepositoryBase<Customer> {

    public CustomerRepository() {
        super(ConnectionFactory.getConnection());
    }

    public CustomerRepository(Connection connection) {
        super(connection);
    }

    @Override
    public Customer get(Object id) throws SQLException {
        String querySqlStr = "SELECT customerId, " +
                                    "customerName, " +
                                    "addressId, " +
                                    "active, " +
                                    "createDate, " +
                                    "createdBy, " +
                                    "lastUpdate, " +
                                    "lastUpdateBy " +
                                "FROM customer " +
                                "WHERE customerId = ?;";
        PreparedStatement queryStatement = null;
        ResultSet resultSet = null;
        Customer customer = null;

        try {
            queryStatement = super.connection.prepareStatement(querySqlStr);
            queryStatement.setInt(1, (int) id);
            resultSet = queryStatement.executeQuery();
            if(resultSet.next()) {
                customer = new Customer();

                customer.setCustomerId(resultSet.getInt("customerId"));
                customer.setCustomerName(resultSet.getString("customerName"));
                customer.setAddressId(resultSet.getInt("addressId"));
                customer.setActive(resultSet.getBoolean("active"));
                customer.setCreateDate(resultSet.getDate("createDate"));
                customer.setCreatedBy(resultSet.getString("createdBy"));
                customer.setLastUpdate(resultSet.getTimestamp("lastUpdate"));
                customer.setLastUpdateBy(resultSet.getString("lastUpdateBy"));
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
        return customer;
    }

    @Override
    public void add(Customer entity) throws SQLException {
        String lastIdQuerySqlStr = "SELECT customerId FROM customer ORDER BY customerId DESC LIMIT 1";
        String insertSqlStr = "INSERT INTO customer (customerId, " +
                                                    "customerName, " +
                                                    "addressId, " +
                                                    "active, " +
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
                                                    "?);";
        PreparedStatement queryStatement = null;
        PreparedStatement insertStatement = null;
        ResultSet resultSet = null;

        try {
            super.startTransaction();
            queryStatement = super.connection.prepareStatement(lastIdQuerySqlStr);
            insertStatement = super.connection.prepareStatement(insertSqlStr);
            resultSet = queryStatement.executeQuery();
            if(resultSet.next()) {
                entity.setCustomerId(resultSet.getInt("customerId") + 1);
            } else {
                entity.setCustomerId(1);
            }
            insertStatement.setInt(1, entity.getCustomerId());
            insertStatement.setString(2, entity.getCustomerName());
            insertStatement.setInt(3, entity.getAddressId());
            insertStatement.setBoolean(4, entity.isActive());
            insertStatement.setDate(5, entity.getCreateDate());
            insertStatement.setString(6, entity.getCreatedBy());
            insertStatement.setTimestamp(7, entity.getLastUpdate());
            insertStatement.setString(8, entity.getLastUpdateBy());
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
    public void update(Customer entity) throws SQLException {
        String updateSqlStr = "UPDATE customer " +
                                "SET customerName = ?, " +
                                    "addressId = ?, " +
                                    "active = ?, " +
                                    "lastUpdate = ?, " +
                                    "lastUpdateBy = ? " +
                                "WHERE customerId = ?;";
        PreparedStatement updateStatement = null;

        try {
            super.startTransaction();
            updateStatement = super.connection.prepareStatement(updateSqlStr);
            updateStatement.setString(1, entity.getCustomerName());
            updateStatement.setInt(2, entity.getAddressId());
            updateStatement.setBoolean(3, entity.isActive());
            updateStatement.setTimestamp(4, entity.getLastUpdate());
            updateStatement.setString(5, entity.getLastUpdateBy());
            updateStatement.setInt(6, entity.getCustomerId());
            int updatedRecords = updateStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if(updateStatement != null) {
                updateStatement.close();
            }
        }
    }

    @Override
    public Customer delete(Object id) throws SQLException {
        Customer customer = this.get(id);
        if(customer != null) {
            String deleteSqlStr = "DELETE FROM customer WHERE customerId = ?;";
            PreparedStatement deleteStatement = null;

            try {
                super.startTransaction();
                deleteStatement = super.connection.prepareStatement(deleteSqlStr);
                deleteStatement.setInt(1, (int) id);
                int updatedRecords = deleteStatement.executeUpdate();
            } catch (SQLException e) {
                throw e;
            } finally {
                if(deleteStatement != null) {
                    deleteStatement.close();
                }
            }
        }
        return customer;
    }
}
