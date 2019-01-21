package edu.wgu.c195.appointments.persistence;

import edu.wgu.c195.appointments.domain.DataAccessException;
import edu.wgu.c195.appointments.domain.DefaultTuple;
import edu.wgu.c195.appointments.domain.Pair;
import edu.wgu.c195.appointments.domain.Tuple;


import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static edu.wgu.c195.appointments.domain.Pair.cons;

public class SQL {

    private static Map<String, Pair<String, Integer>[]> META_DATA_CACHE = new HashMap<>();

    public static void select(Connection connection, String sql, ResultSetProcessor processor, Object... params) {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int cnt = 0;
            for (Object param : params) {
                ps.setObject(++cnt, param);
            }
            try (ResultSet rs = ps.executeQuery()) {
                long rowCnt = 0;
                while (rs.next()) {
                    processor.process(rs, rowCnt++);
                }
            } catch (SQLException e) {
                throw new DataAccessException(e);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public static Tuple rowAsTuple(String sql, ResultSet rs) {
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            Pair<String, Integer>[] columns = null;
            if (!META_DATA_CACHE.containsKey(sql)) {
                synchronized (META_DATA_CACHE) {
                    META_DATA_CACHE.put(sql, getColumns(metaData));
                }
            }
            columns = META_DATA_CACHE.get(sql);
            Collection result = StreamSupport.stream(Spliterators.spliterator(columns, 0), false)
                    .map((Object o) -> {
                        Pair<String, Integer> column = (Pair<String, Integer>) o;
                        try {
                            return cons(column.getCar(), rs.getObject(column.getCar()));
                        } catch (SQLException e) {
                            throw new DataAccessException(e);
                        }
                    }).collect(Collectors.toList());
            return new DefaultTuple(result);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    private static Pair<String, Integer>[] getColumns(ResultSetMetaData rs) {
        int columnCount = 0;
        try {
            columnCount = rs.getColumnCount();
            Pair<String, Integer>[] columns = new Pair[columnCount];
            for (int i = 0; i < columnCount; i++) {
                StringBuilder builder = new StringBuilder();
                String schemaName = rs.getSchemaName(i + 1);
                if(schemaName != null) {
                    schemaName = schemaName.trim();
                }
                String tableName = rs.getTableName(i + 1);
                if(tableName != null) {
                    tableName = tableName.trim();
                }
                String columnName = rs.getColumnName(i + 1);
                if(columnName != null) {
                    columnName = columnName.trim();
                }
                if(schemaName != null && schemaName != "") {
                    builder.append(schemaName).append('.');
                }
                if(tableName != null && tableName != "") {
                    builder.append(tableName).append('.');
                }
                if(columnName != null && columnName != "") {
                    builder.append(columnName);
                }
                String formattedColumnName = builder.toString();
                Integer type = rs.getColumnType(i + 1);
                columns[i] = cons(columnName, type);

            }
            return columns;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public static Stream<Tuple> stream(final Connection connection, final String sql, final Object... parms) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new ResultSetIterator(connection, sql), 0), false);
    }
}
