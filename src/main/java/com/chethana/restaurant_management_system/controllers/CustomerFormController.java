package com.chethana.restaurant_management_system.controllers;

import com.chethana.restaurant_management_system.dto.CustomerDTO;
import com.chethana.restaurant_management_system.model.CustomerModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;

public class CustomerFormController {
    @FXML private TextField customerCodeField;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField contactField;

    private CustomerController customerController;
    private boolean editMode = false;
    private int customerId;
    private String originalCustomerCode;
    private String originalEmail;

    public void setCustomerController(CustomerController controller) {
        this.customerController = controller;
    }

    public void setEditMode(CustomerModel customer) {
        editMode = true;
        customerId = customer.getCustomerId();
        originalCustomerCode = customer.getCustomerCode();
        originalEmail = customer.getEmail();

        customerCodeField.setText(customer.getCustomerCode());
        nameField.setText(customer.getName());
        emailField.setText(customer.getEmail());
        contactField.setText(customer.getContact());
    }

    @FXML
    public void handleSave() {
        if (!validate()) return;

        CustomerModel customer = new CustomerModel();
        customer.setCustomerCode(customerCodeField.getText().trim());
        customer.setName(nameField.getText().trim());
        customer.setEmail(emailField.getText().trim());
        customer.setContact(contactField.getText().trim());

        try {
            if (editMode) {
                customer.setCustomerId(customerId);
                boolean success = CustomerDTO.updateCustomer(customer);
                if (success) {
                    customerController.refreshTable();
                    close();
                }
            } else {
                boolean success = CustomerDTO.saveCustomer(customer);
                if (success) {
                    customerController.refreshTable();
                    close();
                }
            }
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    @FXML
    public void handleCancel() {
        close();
    }

    private boolean validate() {
        if (customerCodeField.getText().trim().isEmpty()) return error("Customer Code required");
        if (nameField.getText().trim().isEmpty()) return error("Name required");

        String email = emailField.getText().trim();
        if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return error("Invalid email format");
        }

        try {
            if (!editMode || !customerCodeField.getText().trim().equals(originalCustomerCode)) {
                if (CustomerDTO.checkCustomerCodeExists(customerCodeField.getText().trim())) {
                    return error("Customer Code already exists");
                }
            }

            if (!email.isEmpty() && (!editMode || !email.equals(originalEmail))) {
                if (CustomerDTO.checkEmailExists(email)) {
                    return error("Email already exists");
                }
            }
        } catch (SQLException e) {
            return error("Database error: " + e.getMessage());
        }

        return true;
    }

    private boolean error(String msg) {
        showError(msg);
        return false;
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private void close() {
        ((Stage) customerCodeField.getScene().getWindow()).close();
    }

}