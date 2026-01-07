package com.chethana.restaurant_management_system.controllers;

import com.chethana.restaurant_management_system.dto.ReservationDTO;
import com.chethana.restaurant_management_system.model.CustomerModel;
import com.chethana.restaurant_management_system.model.ReservationModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ReservationFormController {
    @FXML private ComboBox<CustomerModel> customerComboBox;
    @FXML private DatePicker reservationDatePicker;
    @FXML private TextField reservationTimeField;
    @FXML private Spinner<Integer> guestSpinner;
    @FXML private ComboBox<String> statusComboBox;

    private ReservationController reservationController;
    private boolean editMode = false;
    private int reservationId;

    @FXML
    public void initialize() {
        guestSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 2));
        statusComboBox.getItems().addAll("Pending", "Confirmed", "Cancelled", "Completed");
        statusComboBox.setValue("Pending");
        reservationDatePicker.setValue(LocalDate.now());
        loadCustomers();
    }

    public void setReservationController(ReservationController controller) {
        this.reservationController = controller;
    }

    private void loadCustomers() {
        try {
            List<CustomerModel> customers = ReservationDTO.getAllCustomersForCombo();
            customerComboBox.getItems().clear();
            customerComboBox.getItems().addAll(customers);
        } catch (SQLException e) {
            showError("Failed to load customers: " + e.getMessage());
        }
    }

    public void setEditMode(ReservationModel reservation) {
        editMode = true;
        reservationId = reservation.getReservationId();

        for (CustomerModel customer : customerComboBox.getItems()) {
            if (customer.getCustomerId() == reservation.getCustomerId()) {
                customerComboBox.setValue(customer);
                break;
            }
        }

        reservationDatePicker.setValue(reservation.getReservationDate());
        reservationTimeField.setText(reservation.getReservationTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        guestSpinner.getValueFactory().setValue(reservation.getNumberOfGuests());
        statusComboBox.setValue(reservation.getStatus());
    }

    @FXML
    public void handleSave() {
        if (!validate()) return;

        ReservationModel reservation = new ReservationModel();
        reservation.setCustomerId(customerComboBox.getValue().getCustomerId());
        reservation.setReservationDate(reservationDatePicker.getValue());
        reservation.setReservationTime(LocalTime.parse(reservationTimeField.getText()));
        reservation.setNumberOfGuests(guestSpinner.getValue());
        reservation.setStatus(statusComboBox.getValue());

        try {
            if (editMode) {
                reservation.setReservationId(reservationId);
                boolean success = ReservationDTO.updateReservation(reservation);
                if (success) {
                    reservationController.refreshTable();
                    reservationController.updateStats();
                    close();
                }
            } else {
                boolean success = ReservationDTO.saveReservation(reservation);
                if (success) {
                    reservationController.refreshTable();
                    reservationController.updateStats();
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
        if (customerComboBox.getValue() == null) return error("Please select a customer");
        if (reservationDatePicker.getValue() == null) return error("Please select a reservation date");
        if (reservationTimeField.getText().trim().isEmpty()) return error("Please enter reservation time");

        try {
            LocalTime.parse(reservationTimeField.getText());
        } catch (DateTimeParseException e) {
            return error("Invalid time format. Use HH:mm (e.g., 18:30)");
        }

        if (guestSpinner.getValue() <= 0) return error("Number of guests must be at least 1");
        if (statusComboBox.getValue() == null) return error("Please select a status");

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
        ((Stage) customerComboBox.getScene().getWindow()).close();
    }
}