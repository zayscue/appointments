package edu.wgu.c195.appointments.persistence.repositories;

import edu.wgu.c195.appointments.domain.entities.City;
import edu.wgu.c195.appointments.persistence.ConnectionFactory;
import edu.wgu.c195.appointments.persistence.SQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

public class CityRepository extends RepositoryBase<City> {

    public CityRepository() {
        super(ConnectionFactory.getConnection());
    }

    public CityRepository(Connection connection) {
        super(connection);
    }

    @Override
    public Stream<City> getAll() {
        return SQL.stream(super.connection,
                "SELECT cityId, " +
                            "city, " +
                            "countryId, " +
                            "createDate, " +
                            "createdBy, " +
                            "lastUpdate, " +
                            "lastUpdateBy " +
                        "FROM city")
                .map((t) -> {
                    City city = new City();
                    city.setCityId(t.asInt("cityId"));
                    city.setCity(t.asString("city"));
                    city.setCountryId(t.asInt("countryId"));
                    city.setCreateDate(t.asDate("createDate"));
                    city.setCreatedBy(t.asString("createdBy"));
                    city.setLastUpdate(t.asTimeStamp("lastUpdate"));
                    city.setLastUpdateBy(t.asString("lastUpdateBy"));
                    return city;
                });
    }

    @Override
    public City get(Object id) throws SQLException {
        String querySqlStr = "SELECT cityId, " +
                                    "city, " +
                                    "countryId, " +
                                    "createDate, " +
                                    "createdBy, " +
                                    "lastUpdate, " +
                                    "lastUpdateBy " +
                                "FROM city " +
                                "WHERE cityId = ?;";
        PreparedStatement queryStatement = null;
        ResultSet resultSet = null;
        City city = null;

        try {
            queryStatement = super.connection.prepareStatement(querySqlStr);
            queryStatement.setInt(1, (int) id);
            resultSet = queryStatement.executeQuery();
            if(resultSet.next()) {
                city = new City();

                city.setCityId(resultSet.getInt("cityId"));
                city.setCity(resultSet.getString("city"));
                city.setCountryId(resultSet.getInt("countryId"));
                city.setCreateDate(resultSet.getDate("createDate"));
                city.setCreatedBy(resultSet.getString("createdBy"));
                city.setLastUpdate(resultSet.getTimestamp("lastUpdate"));
                city.setLastUpdateBy(resultSet.getString("lastUpdateBy"));
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

        return city;
    }

    @Override
    public void add(City entity) throws SQLException {
        String lastIdQuerySqlStr = "SELECT cityId FROM city ORDER BY cityId DESC LIMIT 1;";
        String insertSqlStr = "INSERT INTO city (cityId, " +
                                                "city, " +
                                                "countryId, " +
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
                                                "?);";
        PreparedStatement queryStatement = null;
        ResultSet resultSet = null;
        PreparedStatement insertStatement = null;

        try {
            super.startTransaction();
            queryStatement = super.connection.prepareStatement(lastIdQuerySqlStr);
            insertStatement = super.connection.prepareStatement(insertSqlStr);
            resultSet = queryStatement.executeQuery();
            if(resultSet.next()) {
                entity.setCityId(resultSet.getInt("cityId") + 1);
            } else {
                entity.setCityId(1);
            }
            insertStatement.setInt(1, entity.getCityId());
            insertStatement.setString(2, entity.getCity());
            insertStatement.setInt(3, entity.getCountryId());
            insertStatement.setDate(4, entity.getCreateDate());
            insertStatement.setString(5, entity.getCreatedBy());
            insertStatement.setTimestamp(6, entity.getLastUpdate());
            insertStatement.setString(7, entity.getLastUpdateBy());
            int recordsUpdated = insertStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if(resultSet != null) {
                resultSet.close();
            }
            if(queryStatement != null) {
                queryStatement.close();
            }
            if(insertStatement != null) {
                insertStatement.close();
            }
        }
    }

    @Override
    public void update(City entity) throws SQLException {
        String updateSqlStr = "UPDATE city " +
                                "SET city = ?, " +
                                    "countryId = ?, " +
                                    "lastUpdate = ?, " +
                                    "lastUpdateBy = ? " +
                                "WHERE cityId = ?;";
        PreparedStatement updateStatement = null;

        try {
            super.startTransaction();
            updateStatement = super.connection.prepareStatement(updateSqlStr);
            updateStatement.setString(1, entity.getCity());
            updateStatement.setInt(2, entity.getCountryId());
            updateStatement.setTimestamp(3, entity.getLastUpdate());
            updateStatement.setString(4, entity.getLastUpdateBy());
            updateStatement.setInt(5, entity.getCityId());
            int recordsUpdated = updateStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if(updateStatement != null) {
                updateStatement.close();
            }
        }
    }

    @Override
    public City delete(Object id) throws SQLException {
        City city = this.get(id);
        if(city != null) {
            String deleteSqlStr = "DELETE FROM city WHERE cityId = ?;";
            PreparedStatement deleteStatement = null;

            try {
                super.startTransaction();
                deleteStatement = super.connection.prepareStatement(deleteSqlStr);
                deleteStatement.setInt(1, (int) id);
                int recordsUpdated = deleteStatement.executeUpdate();
            } catch (SQLException e) {
                throw e;
            } finally {
                if(deleteStatement != null) {
                    deleteStatement.close();
                }
            }
        }
        return city;
    }
}
