package com.chethana.restaurant_management_system.controllers;

import com.chethana.restaurant_management_system.dto.InventoryDTO;
import com.chethana.restaurant_management_system.model.InventoryModel;
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
import java.sql.Timestamp;
import java.util.List;

public class InventoryController {

    @FXML
    private TableView<InventoryModel> inventoryTable;

    @FXML
    private TableColumn<InventoryModel, Integer> colInventoryId;

    @FXML
    private TableColumn<InventoryModel, String> colItemName;

    @FXML
    private TableColumn<InventoryModel, Integer> colQuantity;

    @FXML
    private TableColumn<InventoryModel, String> colUnit;

    @FXML
    private TableColumn<InventoryModel, Timestamp> colLastUpdated;

    @FXML
    private TextField searchField;

    // ---------------- INITIALIZE ----------------
    @FXML
    public void initialize() {

        colInventoryId.setCellValueFactory(
                cell -> new SimpleIntegerProperty(cell.getValue().getInventoryId()).asObject()
        );

        colItemName.setCellValueFactory(
                cell -> new SimpleStringProperty(cell.getValue().getItemName())
        );

        colQuantity.setCellValueFactory(
                cell -> new SimpleIntegerProperty(cell.getValue().getQuantity()).asObject()
        );

        colUnit.setCellValueFactory(
                cell -> new SimpleStringProperty(cell.getValue().getUnit())
        );

        colLastUpdated.setCellValueFactory(
                cell -> new SimpleObjectProperty<>(cell.getValue().getLastUpdated())
        );

        inventoryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        refreshTable();
    }

    // ---------------- REFRESH ----------------
    public void refreshTable() {
        try {
            List<InventoryModel> list = InventoryDTO.getAllInventory();
            inventoryTable.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    // ---------------- SEARCH ----------------
    @FXML
    public void handleSearch(KeyEvent event) {
        try {
            String search = searchField.getText().trim();
            List<InventoryModel> list;

            if (search.isEmpty()) {
                list = InventoryDTO.getAllInventory();
            } else {
                list = InventoryDTO.searchInventory(search);
            }

            inventoryTable.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    // ---------------- ADD ----------------
    @FXML
    public void handleAddItem() {
        openForm(null);
    }

    // ---------------- EDIT ----------------
    @FXML
    public void handleEditItem() {
        InventoryModel selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select an item");
            return;
        }
        openForm(selected);
    }

    // ---------------- UPDATE STOCK ----------------
    @FXML
    public void handleUpdateStock() {
        handleEditItem();
    }

    // ---------------- DELETE ----------------
    @FXML
    public void handleDeleteItem() {
        InventoryModel selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select an item");
            return;
        }

        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this item?",
                ButtonType.OK,
                ButtonType.CANCEL
        );

        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    if (InventoryDTO.deleteInventory(selected.getInventoryId())) {
                        refreshTable();
                    }
                } catch (SQLException e) {
                    showError(e.getMessage());
                }
            }
        });
    }

    // ---------------- OPEN FORM ----------------
    private void openForm(InventoryModel inventory) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/chethana/restaurant_management_system/inventory_form.fxml")
            );

            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);

            Inventory_formController controller = loader.getController();
            controller.setInventory_formController(this);

            if (inventory != null) {
                controller.setEditMode(inventory);
            }

            stage.showAndWait();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    // ---------------- ERROR ----------------
    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}
