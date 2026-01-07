package com.chethana.restaurant_management_system.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.chethana.restaurant_management_system.dto.InventoryDTO;
import com.chethana.restaurant_management_system.model.CustomerModel;
import com.chethana.restaurant_management_system.model.InventoryModel;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Timestamp;





public class Inventory_formController  {
    public TextField txtinven_Id;
    public TextField txtitem_name;
    public TextField txtqty;
    public TextField txtunit;
    public TextField txttime;
    public Button saveButton;
    public Button cancelButton;
    public TextField invent_Id;
    public DatePicker dplastupdate;

    private InventoryController inventoryController;


    private boolean editMode = false;
    private int inventoryId;
    private String originalItemName;

    public void setInventory_formController(InventoryController controller) {
     this.inventoryController = controller;
     }


    public void setEditMode(InventoryModel inventory) {
        editMode = true;
        inventoryId = inventory.getInventoryId();
        originalItemName = inventory.getItemName();

        txtinven_Id.setText(String.valueOf(inventory.getInventoryId()));
        txtitem_name.setText(inventory.getItemName());
        txtqty.setText(String.valueOf(inventory.getQuantity()));
        txtunit.setText(inventory.getUnit());
        dplastupdate.setValue(
                inventory.getLastUpdated()
                        .toLocalDateTime()
                        .toLocalDate()
        );

    }


    public void handleSave(ActionEvent actionEvent) {

        if (!validate()) return;

        InventoryModel inventory = new InventoryModel();
        inventory.setItemName(txtitem_name.getText().trim());
        inventory.setQuantity(Integer.parseInt(txtqty.getText().trim()));
        inventory.setUnit(txtunit.getText().trim());
        inventory.setLastUpdated(new Timestamp(System.currentTimeMillis()));

        try {
            if (editMode) {
                inventory.setInventoryId(inventoryId); // ONLY for update

                boolean success = InventoryDTO.updateInventory(inventory);
                if (success) {
                    inventoryController.refreshTable();
                    close();
                }
            } else {
                boolean success = InventoryDTO.saveInventory(inventory);
                if (success) {
                    inventoryController.refreshTable();
                    close();
                }
            }
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    private void close() {
        ((Stage) txtitem_name.getScene().getWindow()).close();
    }


    private boolean validate() {
        if (txtitem_name.getText().trim().isEmpty())
            return error("Item Name required");

        if (txtqty.getText().trim().isEmpty())
            return error("Quantity required");

        try {
            Integer.parseInt(txtqty.getText().trim());
        } catch (NumberFormatException e) {
            return error("Quantity must be a number");
        }

        if (txtunit.getText().trim().isEmpty())
            return error("Unit required");

        try {
            if (!editMode || !txtitem_name.getText().trim().equals(originalItemName)) {
                if (InventoryDTO.checkItemNameExists(txtitem_name.getText().trim())) {
                    return error("Item name already exists");
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
    public void handleCancel(ActionEvent actionEvent) {
        ((Stage) txtitem_name.getScene().getWindow()).close();
    }
}
