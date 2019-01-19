package edu.wgu.c195.appointments.persistence.repositories;

import java.sql.SQLException;

public interface IRepository<T> {
    T get(Object id) throws SQLException;
    void add(T entity) throws SQLException;
    void update(T entity) throws SQLException;
    T delete(Object id) throws SQLException;
    void save() throws SQLException;
}
