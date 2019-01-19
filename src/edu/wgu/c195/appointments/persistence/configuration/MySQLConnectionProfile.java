package edu.wgu.c195.appointments.persistence;

public class MySQLConnectionProfile implements IConnectionProfile {
    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    private String server;
    private String database;
    private String user;
    private String password;

    public MySQLConnectionProfile(String server, String database, String user, String password) {
        this.server = server;
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
        return String.format("jdbc:mysql://%1$s:3306/%2$s", this.server, this.database);
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
