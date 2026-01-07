package com.chethana.restaurant_management_system.controllers;

import com.chethana.restaurant_management_system.dto.StaffDTO;
import com.chethana.restaurant_management_system.model.StaffModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;

public class StaffFormController {
    @FXML private TextField staffCodeField;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField contactField;
    @FXML private ComboBox<String> roleCombo;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private DatePicker hireDatePicker;
    @FXML private ComboBox<String> statusCombo;

    private StaffController staffController;
    private boolean editMode = false;
    private int staffId;
    private String originalUsername;
    private String originalStaffCode;

    public void setStaffController(StaffController controller) {
        this.staffController = controller;
    }

    @FXML
    public void initialize() {
        roleCombo.getItems().addAll("Manager", "Chef", "Waiter", "Cashier", "Cleaner", "Admin");
        statusCombo.getItems().addAll("Active", "Inactive", "On Leave");
        statusCombo.setValue("Active");
        hireDatePicker.setValue(LocalDate.now());
    }

    public void setEditMode(StaffModel staff) {
        editMode = true;
        staffId = staff.getStaffId();
        originalUsername = staff.getUsername();
        originalStaffCode = staff.getStaffCode();

        staffCodeField.setText(staff.getStaffCode());
        nameField.setText(staff.getName());
        emailField.setText(staff.getEmail());
        contactField.setText(staff.getContact());
        roleCombo.setValue(staff.getRole());
        usernameField.setText(staff.getUsername());
        passwordField.setText("");
        hireDatePicker.setValue(staff.getHireDate());
        statusCombo.setValue(staff.getStatus());

        passwordField.setPromptText("Leave blank to keep current password");
    }

    @FXML
    public void handleSave() {
        if (!validate()) return;

        StaffModel staff = new StaffModel();
        staff.setStaffCode(staffCodeField.getText().trim());
        staff.setName(nameField.getText().trim());
        staff.setEmail(emailField.getText().trim());
        staff.setContact(contactField.getText().trim());
        staff.setRole(roleCombo.getValue());
        staff.setUsername(usernameField.getText().trim());

        String password = passwordField.getText().trim();
        if (!password.isEmpty()) {
            staff.setPassword(password);
        }

        staff.setHireDate(hireDatePicker.getValue());
        staff.setStatus(statusCombo.getValue());

        try {
            if (editMode) {
                staff.setStaffId(staffId);
                if (password.isEmpty()) {
                    StaffModel existing = StaffDTO.getStaffById(staffId);
                    if (existing != null) {
                        staff.setPassword(existing.getPassword());
                    }
                }

                boolean success = StaffDTO.updateStaff(staff);
                if (success) {
                    staffController.refreshTable();
                    close();
                }
            } else {
                if (password.isEmpty()) {
                    showError("Password is required for new staff");
                    return;
                }
                staff.setPassword(password);

                boolean success = StaffDTO.saveStaff(staff);
                if (success) {
                    staffController.refreshTable();
                    close();
                }
            }
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCancel() {
        close();
    }

    private boolean validate() {
        if (staffCodeField.getText().trim().isEmpty()) return error("Staff Code required");
        if (nameField.getText().trim().isEmpty()) return error("Name required");
        if (emailField.getText().trim().isEmpty()) return error("Email required");
        if (!emailField.getText().trim().matches("^[A-Za-z0-9+_.-]+@(.+)$")) return error("Invalid email format");
        if (roleCombo.getValue() == null) return error("Role required");
        if (usernameField.getText().trim().isEmpty()) return error("Username required");
        if (hireDatePicker.getValue() == null) return error("Hire Date required");
        if (statusCombo.getValue() == null) return error("Status required");

        try {
            if (!editMode || !staffCodeField.getText().trim().equals(originalStaffCode)) {
                if (StaffDTO.checkStaffCodeExists(staffCodeField.getText().trim())) {
                    return error("Staff Code already exists");
                }
            }

            if (!editMode || !usernameField.getText().trim().equals(originalUsername)) {
                if (StaffDTO.checkUsernameExists(usernameField.getText().trim())) {
                    return error("Username already exists");
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
        ((Stage) staffCodeField.getScene().getWindow()).close();
    }
}