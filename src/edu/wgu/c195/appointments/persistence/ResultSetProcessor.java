package edu.wgu.c195.appointments.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetProcessor {

    void process(ResultSet resultSet, long currentRow) throws SQLException;

}
