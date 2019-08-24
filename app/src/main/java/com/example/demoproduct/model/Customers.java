package com.example.demoproduct.model;

public class Customers {
    private int id;
    private String Commission;
    private String CustomerId;
    private String CustomerName;
    private String TMCommission;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommission() {
        return Commission;
    }

    public void setCommission(String commission) {
        Commission = commission;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getTMCommission() {
        return TMCommission;
    }

    public void setTMCommission(String TMCommission) {
        this.TMCommission = TMCommission;
    }
}
