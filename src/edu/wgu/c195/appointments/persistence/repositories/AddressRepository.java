package edu.wgu.c195.appointments.persistence.repositories;

import edu.wgu.c195.appointments.domain.entities.Address;

import java.sql.Connection;
import java.sql.SQLException;

public class AddressRepository extends RepositoryBase<Address> {
    public AddressRepository(Connection connection) {
        super(connection);
    }

    @Override
    public Address get(Object id) throws SQLException {
        return null;
    }

    @Override
    public void add(Address entity) throws SQLException {

    }

    @Override
    public void update(Address entity) throws SQLException {

    }

    @Override
    public Address delete(Object id) throws SQLException {
        return null;
    }
}
