package edu.wgu.c195.appointments.persistence;

import edu.wgu.c195.appointments.persistence.configuration.IConnectionProfile;
import edu.wgu.c195.appointments.persistence.configuration.MySQLConnectionProfile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final IConnectionProfile DEFAULT_CONNECTION_PROFILE = new MySQLConnectionProfile(
            "52.206.157.109",
            "U05M7i",
            "U05M7i",
            "53688541659");

    public static Connection getConnection() throws RuntimeException
    {
        return ConnectionFactory.getConnection(DEFAULT_CONNECTION_PROFILE);
    }

    public static Connection getConnection(IConnectionProfile connectionProfile) throws RuntimeException
    {
        try {
            Class.forName(connectionProfile.getDriver());
            Connection conn = DriverManager.getConnection(
                    connectionProfile.getUrl(),
                    connectionProfile.getUser(),
                    connectionProfile.getPassword()
            );
            return conn;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error connecting to the database", e);
        } catch (SQLException e) {
            throw new RuntimeException("Error loading the driver class", e);
        }
    }

    /**
     * Test Connection
     */
    public static void main(String[] args) {
        Connection connection = ConnectionFactory.getConnection();
    }
}
