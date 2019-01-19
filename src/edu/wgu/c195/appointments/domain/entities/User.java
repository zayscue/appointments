package edu.wgu.c195.appointments.domain.entities;

import java.sql.Date;
import java.sql.Timestamp;

public class User {
    private int userId;
    private String userName;
    private String password;
    private byte active;
    private String createBy;
    private Date createDate;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;

    public User() {

    }

    public User(int userId, String userName, String password, byte active) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.active = active;
    }

    public User(int userId, String userName, String password, byte active, String createBy, Date createDate) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.active = active;
        this.createBy = createBy;
        this.createDate = createDate;
    }

    public User(int userId, String userName, String password, byte active, String createBy, Date createDate, Timestamp lastUpdate, String lastUpdatedBy) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.active = active;
        this.createBy = createBy;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte getActive() {
        return active;
    }

    public void setActive(byte active) {
        this.active = active;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
}
