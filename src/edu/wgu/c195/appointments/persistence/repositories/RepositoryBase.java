package edu.wgu.c195.appointments.persistence.repositories;

import java.sql.Connection;
import java.sql.SQLException;

abstract class RepositoryBase<T> implements IRepository<T>  {
    protected final Connection connection;
    private boolean transactionStarted;

    protected RepositoryBase(Connection connection) {
        this.connection = connection;
    }

    protected void startTransaction() throws SQLException {
        if(!this.transactionStarted) {
            this.connection.setAutoCommit(false);
            this.transactionStarted = true;
        }
    }

    @Override
    public void save() throws SQLException {
        this.connection.commit();
        this.connection.setAutoCommit(false);
        this.transactionStarted = false;
    }
}
