package edu.wgu.c195.appointments.persistence.configuration;

public interface IConnectionProfile {
    String getDriver();
    String getUrl();
    String getUser();
    String getPassword();
}
