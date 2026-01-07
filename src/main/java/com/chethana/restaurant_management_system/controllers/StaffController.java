package com.chethana.restaurant_management_system.controllers;

import com.chethana.restaurant_management_system.dto.StaffDTO;
import com.chethana.restaurant_management_system.model.StaffModel;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import java.util.List;

public class StaffController {
    @FXML private TableView<StaffModel> staffTable;
    @FXML private TableColumn<StaffModel, Integer> colStaffId;
    @FXML private TableColumn<StaffModel, String> colStaffCode;
    @FXML private TableColumn<StaffModel, String> colName;
    @FXML private TableColumn<StaffModel, String> colEmail;
    @FXML private TableColumn<StaffModel, String> colContact;
    @FXML private TableColumn<StaffModel, String> colRole;
    @FXML private TableColumn<StaffModel, String> colStatus;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> roleFilter;

    @FXML
    public void initialize() {
        colStaffId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStaffId()).asObject());
        colStaffCode.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStaffCode()));
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        colEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        colContact.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContact()));
        colRole.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole()));
        colStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        staffTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        refreshTable();
        loadRoleFilter();
    }

    public void refreshTable() {
        try {
            List<StaffModel> list = StaffDTO.getAllStaff();
            staffTable.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    private void loadRoleFilter() {
        try {
            roleFilter.getItems().clear();
            roleFilter.getItems().add("All Roles");
            roleFilter.getItems().addAll(StaffDTO.getDistinctRoles());
            roleFilter.setValue("All Roles");
        } catch (SQLException e) {
            showError(e.getMessage());
        }
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
            String role = roleFilter.getValue();
            List<StaffModel> list;
            if ("All Roles".equals(role)) {
                list = search.isEmpty() ? StaffDTO.getAllStaff() : StaffDTO.searchStaff(search);
            } else {
                list = StaffDTO.getStaffByRole(role);
            }
            staffTable.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    public void handleAddStaff() {
        openForm(null);
    }

    @FXML
    public void handleEditStaff() {
        StaffModel selected = staffTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a staff member");
            return;
        }
        openForm(selected);
    }

    @FXML
    public void handleDeleteStaff() {
        StaffModel selected = staffTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a staff member");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Staff Member");
        confirm.setContentText("Are you sure you want to delete " + selected.getName() + "?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    if (StaffDTO.deleteStaff(selected.getStaffId())) {
                        refreshTable();
                    }
                } catch (SQLException e) {
                    showError(e.getMessage());
                }
            }
        });
    }

    private void openForm(StaffModel staff) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/chethana/restaurant_management_system/staff_form.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            StaffFormController controller = loader.getController();
            controller.setStaffController(this);
            if (staff != null) controller.setEditMode(staff);
            stage.showAndWait();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}