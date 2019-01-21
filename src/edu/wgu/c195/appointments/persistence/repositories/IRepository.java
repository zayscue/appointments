package edu.wgu.c195.appointments.persistence.repositories;

import edu.wgu.c195.appointments.domain.Tuple;

import java.sql.SQLException;
import java.util.stream.Stream;

public interface IRepository<T> {
    Stream<T> getAll();
    T get(Object id) throws SQLException;
    void add(T entity) throws SQLException;
    void update(T entity) throws SQLException;
    T delete(Object id) throws SQLException;
    void save() throws SQLException;
}
