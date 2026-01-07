package com.chethana.restaurant_management_system.model;

import java.time.LocalDate;

public class paymentModel {

    private int paymentId;
    private int orderId;
    private String customerName;
    private String paymentMethod;
    private double amount;
    private LocalDate paymentDate;

    // -----------------------------
    // CONSTRUCTORS
    // -----------------------------

    public paymentModel() {
    }

    public paymentModel(int paymentId, int orderId, String customerName,
                        String paymentMethod, double amount, LocalDate paymentDate) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.customerName = customerName;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }

    // -----------------------------
    // GETTERS & SETTERS
    // -----------------------------

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
}
