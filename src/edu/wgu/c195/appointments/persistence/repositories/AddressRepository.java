package edu.wgu.c195.appointments.persistence.repositories;

import edu.wgu.c195.appointments.domain.entities.Address;
import edu.wgu.c195.appointments.persistence.ConnectionFactory;
import edu.wgu.c195.appointments.persistence.SQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

public class AddressRepository extends RepositoryBase<Address> {

    public AddressRepository() {
        super(ConnectionFactory.getConnection());
    }

    public AddressRepository(Connection connection) {
        super(connection);
    }

    @Override
    public Stream<Address> getAll() {
        return SQL.stream(super.connection,
                "SELECT addressId, " +
                            "address, " +
                            "address2, " +
                            "cityId, " +
                            "postalCode, " +
                            "phone, " +
                            "createDate, " +
                            "createdBy, " +
                            "lastUpdate, " +
                            "lastUpdateBy " +
                        "FROM address")
                .map((t) -> {
                    Address address = new Address();
                    address.setAddressId(t.asInt("addressId"));
                    address.setAddress(t.asString("address"));
                    address.setAddress2(t.asString("address2"));
                    address.setCityId(t.asInt("cityId"));
                    address.setPostalCode(t.asString("postalCode"));
                    address.setPhone(t.asString("phone"));
                    address.setCreateDate(t.asDate("createDate"));
                    address.setCreatedBy(t.asString("createdBy"));
                    address.setLastUpdate(t.asTimeStamp("lastUpdate"));
                    address.setLastUpdateBy(t.asString("lastUpdateBy"));
                    return address;
                });
    }

    @Override
    public Address get(Object id) throws SQLException {
        String querySqlStr = "SELECT addressId, " +
                                    "address, " +
                                    "address2, " +
                                    "cityId, " +
                                    "postalCode, " +
                                    "phone, " +
                                    "createDate, " +
                                    "createdBy, " +
                                    "lastUpdate, " +
                                    "lastUpdateBy " +
                                "FROM address " +
                                "WHERE addressId = ?;";
        PreparedStatement queryStatement = null;
        ResultSet resultSet = null;
        Address address = null;

        try {
            queryStatement = super.connection.prepareStatement(querySqlStr);
            queryStatement.setInt(1, (int) id);
            resultSet = queryStatement.executeQuery();
            if(resultSet.next()) {
                address = new Address();

                address.setAddressId(resultSet.getInt("addressId"));
                address.setAddress(resultSet.getString("address"));
                address.setAddress2(resultSet.getString("address2"));
                address.setCityId(resultSet.getInt("cityId"));
                address.setPostalCode(resultSet.getString("postalCode"));
                address.setPhone(resultSet.getString("phone"));
                address.setCreateDate(resultSet.getDate("createDate"));
                address.setCreatedBy(resultSet.getString("createdBy"));
                address.setLastUpdate(resultSet.getTimestamp("lastUpdate"));
                address.setLastUpdateBy(resultSet.getString("lastUpdateBy"));
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if(resultSet != null) {
                resultSet.close();
            }
            if(queryStatement != null) {
                queryStatement.close();
            }
        }
        return address;
    }

    @Override
    public void add(Address entity) throws SQLException {
        String lastIdQuerySqlStr = "SELECT addressId FROM address ORDER BY addressId DESC LIMIT 1;";
        String insertSqlStr = "INSERT INTO address (addressId, " +
                                                    "address, " +
                                                    "address2, " +
                                                    "cityId, " +
                                                    "postalCode, " +
                                                    "phone, " +
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
                entity.setAddressId(resultSet.getInt("addressId") + 1);
            } else {
                entity.setAddressId(1);
            }
            insertStatement.setInt(1, entity.getAddressId());
            insertStatement.setString(2, entity.getAddress());
            insertStatement.setString(3, entity.getAddress2());
            insertStatement.setInt(4, entity.getCityId());
            insertStatement.setString(5, entity.getPostalCode());
            insertStatement.setString(6, entity.getPhone());
            insertStatement.setDate(7, entity.getCreateDate());
            insertStatement.setString(8, entity.getCreatedBy());
            insertStatement.setTimestamp(9, entity.getLastUpdate());
            insertStatement.setString(10, entity.getLastUpdateBy());
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
            if(resultSet != null) {
                resultSet.close();
            }
        }
    }

    @Override
    public void update(Address entity) throws SQLException {
        String updateSqlStr = "UPDATE address " +
                                "SET address = ?, " +
                                    "address2 = ?, " +
                                    "cityId = ?, " +
                                    "postalCode = ?, " +
                                    "phone = ?, " +
                                    "lastUpdate = ?, " +
                                    "lastUpdateBy = ? " +
                                "WHERE addressId = ?;";
        PreparedStatement updateStatement = null;

        try {
            super.startTransaction();
            updateStatement = super.connection.prepareStatement(updateSqlStr);
            updateStatement.setString(1, entity.getAddress());
            updateStatement.setString(2, entity.getAddress2());
            updateStatement.setInt(3, entity.getCityId());
            updateStatement.setString(4, entity.getPostalCode());
            updateStatement.setString(5, entity.getPhone());
            updateStatement.setTimestamp(6, entity.getLastUpdate());
            updateStatement.setString(7, entity.getLastUpdateBy());
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
    public Address delete(Object id) throws SQLException {
        Address address = this.get(id);
        if(address != null) {
            String deleteSqlStr = "DELETE FROM address WHERE addressId = ?;";
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
        return address;
    }
}
