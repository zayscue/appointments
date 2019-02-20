package edu.wgu.c195.appointments.persistence;

import edu.wgu.c195.appointments.domain.exceptions.DataAccessException;
import edu.wgu.c195.appointments.domain.Tuple;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

public class ResultSetIterator implements Iterator {

    private ResultSet rs;
    private PreparedStatement ps;
    private Connection connection;
    private String sql;

    public ResultSetIterator(Connection connection, String sql) {
        assert connection != null;
        assert sql != null;
        this.connection = connection;
        this.sql = sql;
    }

    public void init() {
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

        } catch (SQLException e) {
            close();
            throw new DataAccessException(e);
        }
    }

    @Override
    public boolean hasNext() {
        if (ps == null) {
            init();
        }
        try {
            boolean hasMore = rs.next();
            if (!hasMore) {
                close();
            }
            return hasMore;
        } catch (SQLException e) {
            close();
            throw new DataAccessException(e);
        }

    }

    private void close() {
        try {
            rs.close();
            try {
                ps.close();
            } catch (SQLException e) {
                //nothing we can do here
            }
        } catch (SQLException e) {
            //nothing we can do here
        }
    }

    @Override
    public Tuple next() {
        try {
            return SQL.rowAsTuple(sql, rs);
        } catch (DataAccessException e) {
            close();
            throw e;
        }
    }
}
