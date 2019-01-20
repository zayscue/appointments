package edu.wgu.c195.appointments.persistence.repositories;

import edu.wgu.c195.appointments.domain.entities.Country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryRepository extends RepositoryBase<Country> {
    public CountryRepository(Connection connection) {
        super(connection);
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

        PreparedStatement queryStatement = super.connection.prepareStatement(querySqlStr);
        queryStatement.setInt(1, (int) id);
        ResultSet resultSet = queryStatement.executeQuery();
        Country country = null;
        if(resultSet.next()) {
            country = new Country();

            country.setCountryId(resultSet.getInt("countryId"));
            country.setCountry(resultSet.getString("country"));
            country.setCreateDate(resultSet.getDate("createDate"));
            country.setCreatedBy(resultSet.getString("createdBy"));
            country.setLastUpdate(resultSet.getTimestamp("lastUpdate"));
            country.setLastUpdateBy(resultSet.getString("lastUpdateBy"));
        }
        queryStatement.close();

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

        super.startTransaction();
        PreparedStatement queryStatement = super.connection.prepareStatement(lastIdQuerySqlStr);
        PreparedStatement insertStatement = super.connection.prepareStatement(insertSqlStr);
        ResultSet resultSet = queryStatement.executeQuery();
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
        insertStatement.executeUpdate();

        queryStatement.close();
        insertStatement.close();
    }

    @Override
    public void update(Country entity) throws SQLException {
        String updateSqlStr = "UPDATE country " +
                                "SET country = ?, " +
                                    "lastUpdate = ?, " +
                                    "lastUpdateBy = ? " +
                                "WHERE countryId = ?;";

        super.startTransaction();
        PreparedStatement updateStatement = super.connection.prepareStatement(updateSqlStr);
        updateStatement.setString(1, entity.getCountry());
        updateStatement.setTimestamp(2, entity.getLastUpdate());
        updateStatement.setString(3, entity.getLastUpdateBy());
        updateStatement.setInt(4, entity.getCountryId());
        updateStatement.executeUpdate();

        updateStatement.close();
    }

    @Override
    public Country delete(Object id) throws SQLException {
        Country country = this.get(id);
        if(country != null) {
            String deleteSqlStr = "DELETE FROM country WHERE countryId = ?;";
            super.startTransaction();
            PreparedStatement deleteStatement = super.connection.prepareStatement(deleteSqlStr);
            deleteStatement.setInt(1, (int) id);
            deleteStatement.executeUpdate();

            deleteStatement.close();
        }
        return country;
    }
}
