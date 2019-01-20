package edu.wgu.c195.appointments.persistence.repositories;

import edu.wgu.c195.appointments.domain.entities.City;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CityRepository extends RepositoryBase<City> {
    public CityRepository(Connection connection) {
        super(connection);
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

        PreparedStatement queryStatement = super.connection.prepareStatement(querySqlStr);
        queryStatement.setInt(1, (int) id);
        ResultSet resultSet = queryStatement.executeQuery();
        City city = null;
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
        queryStatement.close();

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

        super.startTransaction();
        PreparedStatement queryStatement = super.connection.prepareStatement(lastIdQuerySqlStr);
        PreparedStatement insertStatement = super.connection.prepareStatement(insertSqlStr);
        ResultSet resultSet = queryStatement.executeQuery();
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
        insertStatement.executeUpdate();

        queryStatement.close();
        insertStatement.close();
    }

    @Override
    public void update(City entity) throws SQLException {
        String updateSqlStr = "UPDATE city " +
                                "SET city = ?, " +
                                    "countryId = ?, " +
                                    "lastUpdate = ?, " +
                                    "lastUpdateBy = ? " +
                                "WHERE cityId = ?;";

        super.startTransaction();
        PreparedStatement updateStatement = super.connection.prepareStatement(updateSqlStr);
        updateStatement.setString(1, entity.getCity());
        updateStatement.setInt(2, entity.getCountryId());
        updateStatement.setTimestamp(3, entity.getLastUpdate());
        updateStatement.setString(4, entity.getLastUpdateBy());
        updateStatement.setInt(5, entity.getCityId());
        updateStatement.executeUpdate();

        updateStatement.close();
    }

    @Override
    public City delete(Object id) throws SQLException {
        City city = this.get(id);
        if(city != null) {
            String deleteSqlStr = "DELETE FROM city WHERE cityId = ?;";

            super.startTransaction();
            PreparedStatement deleteStatement = super.connection.prepareStatement(deleteSqlStr);
            deleteStatement.setInt(1, (int) id);
            deleteStatement.executeUpdate();

            deleteStatement.close();
        }
        return city;
    }
}
