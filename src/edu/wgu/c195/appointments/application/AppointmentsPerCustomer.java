package edu.wgu.c195.appointments.application;

public class AppointmentsPerCustomer {
    private final String customerName;

    public String getCustomerName() {
        return customerName;
    }

    public int getCount() {
        return count;
    }

    private final int count;

    public AppointmentsPerCustomer(String customerName, int count) {
        this.customerName = customerName;
        this.count = count;
    }
}
