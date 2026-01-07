package com.chethana.restaurant_management_system.model;

public class CustomerModel {
    private int customerId;
    private String customerCode;
    private String name;
    private String email;
    private String contact;

    // Default constructor
    public CustomerModel() {
    }

    // Constructor for combo box
    public CustomerModel(int customerId, String name, String contact) {
        this.customerId = customerId;
        this.name = name;
        this.contact = contact;
    }

    // Full constructor
    public CustomerModel(int customerId, String customerCode, String name, String email, String contact) {
        this.customerId = customerId;
        this.customerCode = customerCode;
        this.name = name;
        this.email = email;
        this.contact = contact;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return name + " (" + contact + ")";
    }
}