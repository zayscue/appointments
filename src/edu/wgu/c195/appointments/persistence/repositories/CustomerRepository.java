package edu.wgu.c195.appointments.persistence.repositories;

import edu.wgu.c195.appointments.domain.entities.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRepository extends RepositoryBase<Customer> {

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
        PreparedStatement queryStatement = super.connection.prepareStatement(querySqlStr);
        queryStatement.setInt(1, (int) id);
        ResultSet resultSet = queryStatement.executeQuery();
        Customer customer = null;
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
        queryStatement.close();
        return customer;
    }

    @Override
    public void add(Customer entity) throws SQLException {
        String lastIdSqlQueryStr = "SELECT customerId FROM customer ORDER BY customerId DESC LIMIT 1";
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

        super.startTransaction();
        PreparedStatement queryStatement = super.connection.prepareStatement(lastIdSqlQueryStr);
        PreparedStatement insertStatement = super.connection.prepareStatement(insertSqlStr);
        ResultSet resultSet = queryStatement.executeQuery();
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

        queryStatement.close();
        insertStatement.close();
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
        super.startTransaction();
        PreparedStatement updateStatement = super.connection.prepareStatement(updateSqlStr);
        updateStatement.setString(1, entity.getCustomerName());
        updateStatement.setInt(2, entity.getAddressId());
        updateStatement.setBoolean(3, entity.isActive());
        updateStatement.setTimestamp(4, entity.getLastUpdate());
        updateStatement.setString(5, entity.getLastUpdateBy());
        updateStatement.setInt(6, entity.getCustomerId());
        updateStatement.executeUpdate();

        updateStatement.close();
    }

    @Override
    public Customer delete(Object id) throws SQLException {
        Customer customer = this.get(id);
        if(customer != null) {
            String deleteSqlStr = "DELETE FROM customer WHERE customerId = ?;";
            super.startTransaction();
            PreparedStatement deleteStatement = super.connection.prepareStatement(deleteSqlStr);
            deleteStatement.setInt(1, (int) id);
            deleteStatement.executeUpdate();

            deleteStatement.close();
        }
        return customer;
    }
}
