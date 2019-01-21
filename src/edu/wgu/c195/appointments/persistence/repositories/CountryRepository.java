package edu.wgu.c195.appointments.persistence.repositories;

import edu.wgu.c195.appointments.domain.entities.Country;
import edu.wgu.c195.appointments.persistence.ConnectionFactory;
import edu.wgu.c195.appointments.persistence.SQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

public class CountryRepository extends RepositoryBase<Country> {

    public CountryRepository() {
        super(ConnectionFactory.getConnection());
    }

    public CountryRepository(Connection connection) {
        super(connection);
    }

    @Override
    public Stream<Country> getAll() {
        return SQL.stream(super.connection,
                "SELECT countryId, " +
                            "country, " +
                            "createDate, " +
                            "createdBy, " +
                            "lastUpdate, " +
                            "lastUpdateBy " +
                        "FROM country")
                .map((t) -> {
                   Country country = new Country();
                   country.setCountryId(t.asInt("countryId"));
                   country.setCountry(t.asString("country"));
                   country.setCreateDate(t.asDate("createDate"));
                   country.setCreatedBy(t.asString("createdBy"));
                   country.setLastUpdate(t.asTimeStamp("lastUpdate"));
                   country.setLastUpdateBy(t.asString("lastUpdateBy"));
                   return country;
                });
    }

    @Override
    public Country get(Object id) throws SQLException {
        String querySqlStr = "SELECT countryId, " +
                                    "country, " +
                                    "createDate, " +
                                    "createdBy, " +
                                    "lastUpdate, " +
                                    "lastUpdateBy " +
                                "FROM country " +
                                "WHERE countryId = ?;";
        PreparedStatement queryStatement = null;
        ResultSet resultSet = null;
        Country country = null;

        try {
            queryStatement = super.connection.prepareStatement(querySqlStr);
            queryStatement.setInt(1, (int) id);
            resultSet = queryStatement.executeQuery();
            country = null;
            if(resultSet.next()) {
                country = new Country();

                country.setCountryId(resultSet.getInt("countryId"));
                country.setCountry(resultSet.getString("country"));
                country.setCreateDate(resultSet.getDate("createDate"));
                country.setCreatedBy(resultSet.getString("createdBy"));
                country.setLastUpdate(resultSet.getTimestamp("lastUpdate"));
                country.setLastUpdateBy(resultSet.getString("lastUpdateBy"));
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if(queryStatement != null) {
                queryStatement.close();
            }
            if(resultSet != null) {
                resultSet.close();
            }
        }
        return country;
    }

    @Override
    public void add(Country entity) throws SQLException {
        String lastIdQuerySqlStr = "SELECT countryId FROM country ORDER BY countryId DESC LIMIT 1";
        String insertSqlStr = "INSERT INTO country (countryId, " +
                                                    "country, " +
                                                    "createDate, " +
                                                    "createdBy, " +
                                                    "lastUpdate, " +
                                                    "lastUpdateBy) " +
                                            "VALUES (?, " +
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
                entity.setCountryId(resultSet.getInt("countryId") + 1);
            } else {
                entity.setCountryId(1);
            }
            insertStatement.setInt(1, entity.getCountryId());
            insertStatement.setString(2, entity.getCountry());
            insertStatement.setDate(3, entity.getCreateDate());
            insertStatement.setString(4, entity.getCreatedBy());
            insertStatement.setTimestamp(5, entity.getLastUpdate());
            insertStatement.setString(6, entity.getLastUpdateBy());
            int updatedRecords = insertStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if(queryStatement != null) {
                queryStatement.close();
            }
            if(resultSet != null) {
                resultSet.close();
            }
            if(insertStatement != null) {
                insertStatement.close();
            }
        }
    }

    @Override
    public void update(Country entity) throws SQLException {
        String updateSqlStr = "UPDATE country " +
                                "SET country = ?, " +
                                    "lastUpdate = ?, " +
                                    "lastUpdateBy = ? " +
                                "WHERE countryId = ?;";
        PreparedStatement updateStatement = null;

        try {
            super.startTransaction();
            updateStatement = super.connection.prepareStatement(updateSqlStr);
            updateStatement.setString(1, entity.getCountry());
            updateStatement.setTimestamp(2, entity.getLastUpdate());
            updateStatement.setString(3, entity.getLastUpdateBy());
            updateStatement.setInt(4, entity.getCountryId());
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
    public Country delete(Object id) throws SQLException {
        Country country = this.get(id);
        if(country != null) {
            String deleteSqlStr = "DELETE FROM country WHERE countryId = ?;";
            PreparedStatement deleteStatement = null;

            try {
                super.startTransaction();
                deleteStatement = super.connection.prepareStatement(deleteSqlStr);
                deleteStatement.setInt(1, (int) id);
                deleteStatement.executeUpdate();
            } catch (SQLException e) {
                throw e;
            } finally {
                if(deleteStatement != null) {
                    deleteStatement.close();
                }
            }
        }
        return country;
    }
}
