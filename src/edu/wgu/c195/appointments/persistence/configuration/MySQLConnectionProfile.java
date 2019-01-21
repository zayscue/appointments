package edu.wgu.c195.appointments.persistence.configuration;

import edu.wgu.c195.appointments.persistence.configuration.IConnectionProfile;

public class MySQLConnectionProfile implements IConnectionProfile {
    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    private static final int DEFAULT_PORT = 3306;

    private String server;
    private int port;
    private String database;
    private String user;
    private String password;

    public MySQLConnectionProfile(String server, String database, String user, String password) {
        this.server = server;
        this.port = DEFAULT_PORT;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public MySQLConnectionProfile(String server, int port, String database, String user, String password) {
        this.server = server;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    @Override
    public String getDriver() {
        return MYSQL_DRIVER;
    }

    @Override
    public String getUrl() {
        return String.format("jdbc:mysql://%1$s:%2$d/%3$s", this.server, this.port, this.database);
    }

    @Override
    public String getUser() {
        return this.user;
    }

    @Override
    public String getPassword() {
        return this.password;
    }
}
