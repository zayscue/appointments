package edu.wgu.c195.appointments.domain.entities;

import edu.wgu.c195.appointments.domain.ValidationResult;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int customerId;
    private String customerName;
    private int addressId;
    private boolean active;
    private Date createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;

    public Customer() {

    }

    public Customer(int customerId, String customerName, int addressId, boolean active) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.addressId = addressId;
        this.active = active;
    }

    public Customer(int customerId, String customerName, int addressId, boolean active, Date createDate, String createdBy) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.addressId = addressId;
        this.active = active;
        this.createDate = createDate;
        this.createdBy = createdBy;
    }

    public Customer(int customerId, String customerName, int addressId, boolean active, Date createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.addressId = addressId;
        this.active = active;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    @Override
    public String toString() {
        return this.customerName;
    }

    public ValidationResult validate() {
        List<String> errors = new ArrayList<>();
        if (this.customerName == null || this.customerName.trim().equals("")) {
            errors.add("The customer name can't be empty.");
        }
        if (this.addressId <= 0) {
            errors.add("The addressId can't be less than or equal to zero.");
        }
        if (errors.size() > 0) {
            return new ValidationResult(errors);
        } else {
            return new ValidationResult();
        }
    }
}
