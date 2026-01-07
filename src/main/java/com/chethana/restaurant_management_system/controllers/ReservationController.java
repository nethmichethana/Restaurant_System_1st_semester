package com.chethana.restaurant_management_system.controllers;

import com.chethana.restaurant_management_system.dto.ReservationDTO;
import com.chethana.restaurant_management_system.model.ReservationModel;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservationController {
    @FXML private TableView<ReservationModel> reservationTable;
    @FXML private TableColumn<ReservationModel, Integer> colReservationId;
    @FXML private TableColumn<ReservationModel, String> colCustomerName;
    @FXML private TableColumn<ReservationModel, String> colContactNumber;
    @FXML private TableColumn<ReservationModel, String> colReservationTime;
    @FXML private TableColumn<ReservationModel, Integer> colGuests;
    @FXML private TableColumn<ReservationModel, String> colStatus;

    @FXML private TextField searchField;
    @FXML private DatePicker dateFilter;
    @FXML private ComboBox<String> statusFilter;

    @FXML private Label totalReservationsLabel;
    @FXML private Label confirmedLabel;
    @FXML private Label pendingLabel;
    @FXML private Label todayLabel;
    @FXML private Label totalGuestsLabel;
    @FXML private Label statusLabel;
    @FXML private Label recordCountLabel;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    public void initialize() {
        colReservationId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getReservationId()).asObject());
        colCustomerName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerName()));
        colContactNumber.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerContact()));

        colReservationTime.setCellValueFactory(cellData -> {
            String dateTime = cellData.getValue().getReservationDate().format(dateFormatter) + " " +
                    cellData.getValue().getReservationTime().format(timeFormatter);
            return new SimpleStringProperty(dateTime);
        });

        colGuests.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumberOfGuests()).asObject());
        colStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        reservationTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        setupStatusFilter();
        refreshTable();
        updateStats();
    }

    private void setupStatusFilter() {
        try {
            statusFilter.getItems().clear();
            statusFilter.getItems().add("All Status");
            List<String> statuses = ReservationDTO.getDistinctStatus();
            if (statuses != null && !statuses.isEmpty()) {
                statusFilter.getItems().addAll(statuses);
            }
            statusFilter.setValue("All Status");
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    public void refreshTable() {
        try {
            List<ReservationModel> list = ReservationDTO.getAllReservations();
            reservationTable.setItems(FXCollections.observableArrayList(list));
            updateRecordCount(list.size());
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    public void updateStats() {
        try {
            int total = ReservationDTO.getTotalReservationsCount();
            int confirmed = ReservationDTO.getReservationsCountByStatus("Confirmed");
            int pending = ReservationDTO.getReservationsCountByStatus("Pending");
            int today = ReservationDTO.getTodayReservationsCount();
            int totalGuests = ReservationDTO.getTotalGuestsCount();

            totalReservationsLabel.setText(String.valueOf(total));
            confirmedLabel.setText(String.valueOf(confirmed));
            pendingLabel.setText(String.valueOf(pending));
            todayLabel.setText(String.valueOf(today));
            totalGuestsLabel.setText(String.valueOf(totalGuests));
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    private void updateRecordCount(int count) {
        recordCountLabel.setText("Showing " + count + " reservations");
    }

    @FXML
    public void handleSearch(KeyEvent event) {
        filterTable();
    }

    @FXML
    public void handleSearch() {
        filterTable();
    }

    private void filterTable() {
        try {
            String search = searchField.getText().trim();
            LocalDate date = dateFilter.getValue();
            String status = statusFilter.getValue();

            List<ReservationModel> list;

            if (date != null && !"All Status".equals(status)) {
                list = getReservationsByDateAndStatus(date, status);
            } else if (date != null) {
                list = ReservationDTO.getReservationsByDate(date);
            } else if (!"All Status".equals(status)) {
                list = ReservationDTO.getReservationsByStatus(status);
            } else if (!search.isEmpty()) {
                list = ReservationDTO.searchReservations(search);
            } else {
                list = ReservationDTO.getAllReservations();
            }

            reservationTable.setItems(FXCollections.observableArrayList(list));
            updateRecordCount(list.size());
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    private List<ReservationModel> getReservationsByDateAndStatus(LocalDate date, String status) throws SQLException {
        String sql = "SELECT r.*, c.name as customer_name, c.contact as customer_contact FROM reservation r LEFT JOIN customer c ON r.customer_id = c.customer_id WHERE r.date=? AND r.status=? ORDER BY r.time";
        java.sql.ResultSet rs = com.chethana.restaurant_management_system.util.CrudUtil.executeQuery(sql, date, status);
        List<ReservationModel> list = new java.util.ArrayList<>();
        while (rs.next()) {
            ReservationModel reservation = new ReservationModel();
            reservation.setReservationId(rs.getInt("reservation_id"));
            reservation.setCustomerId(rs.getInt("customer_id"));
            reservation.setCustomerName(rs.getString("customer_name"));
            reservation.setCustomerContact(rs.getString("customer_contact"));

            reservation.setReservationDate(rs.getDate("date").toLocalDate());
            reservation.setReservationTime(rs.getTime("time").toLocalTime());

            reservation.setNumberOfGuests(rs.getInt("number_of_guests"));
            reservation.setStatus(rs.getString("status"));
            list.add(reservation);
        }
        return list;
    }

    @FXML
    public void handleAddReservation() {
        openForm(null);
    }

    @FXML
    public void handleEditReservation() {
        ReservationModel selected = reservationTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a reservation");
            return;
        }
        openForm(selected);
    }

    @FXML
    public void handleConfirmReservation() {
        ReservationModel selected = reservationTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a reservation");
            return;
        }
        try {
            if (ReservationDTO.updateReservationStatus(selected.getReservationId(), "Confirmed")) {
                refreshTable();
                updateStats();
                statusLabel.setText("Reservation confirmed successfully");
            }
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    public void handleCancelReservation() {
        ReservationModel selected = reservationTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a reservation");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Cancel");
        confirm.setHeaderText("Cancel Reservation");
        confirm.setContentText("Are you sure you want to cancel this reservation?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    if (ReservationDTO.updateReservationStatus(selected.getReservationId(), "Cancelled")) {
                        refreshTable();
                        updateStats();
                        statusLabel.setText("Reservation cancelled successfully");
                    }
                } catch (SQLException e) {
                    showError(e.getMessage());
                }
            }
        });
    }

    @FXML
    public void handleRefresh() {
        refreshTable();
        updateStats();
        statusLabel.setText("Data refreshed");
    }

    @FXML
    public void handleViewCalendar() {
        showError("Calendar view feature not implemented yet");
    }

    private void openForm(ReservationModel reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/chethana/restaurant_management_system/reservation_form.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            ReservationFormController controller = loader.getController();
            controller.setReservationController(this);
            if (reservation != null) controller.setEditMode(reservation);
            stage.showAndWait();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void showError(String msg) {
        System.out.println(msg);
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}