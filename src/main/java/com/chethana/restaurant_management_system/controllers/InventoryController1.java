/*
package com.chethana.restaurant_management_system.controllers;

import com.chethana.restaurant_management_system.dto.InventoryDTO;

import com.chethana.restaurant_management_system.model.InventoryModel;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
//import javafx.scene.Scene;
import java.sql.SQLException;
import java.util.List;
import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;

public class InventoryController {

    public TableColumn colInventoryId;
    @FXML private TableView<InventoryModel> inventoryTable;

    @FXML private TableColumn<InventoryModel, Integer> colId;
    @FXML private TableColumn<InventoryModel, String> colItemName;
    @FXML private TableColumn<InventoryModel, Integer> colQuantity;
    @FXML private TableColumn<InventoryModel, String> colUnit;
    @FXML private TableColumn<InventoryModel, String> colLastUpdated;

    @FXML private TextField txtItemName;
    @FXML private TextField txtQuantity;
    @FXML private TextField txtUnit;
    @FXML private TextField searchField;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getInventoryId()).asObject());

        colItemName.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getItemName()));

        colQuantity.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getQuantity()).asObject());

        colUnit.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getUnit()));

        colLastUpdated.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getLastUpdated() != null
                                ? c.getValue().getLastUpdated().toString()
                                : ""
                ));

        inventoryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        refreshTable();
    }

    private void refreshTable() {
        try {
            List<InventoryModel> list = InventoryDTO.getAll();
            inventoryTable.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    public void handleAdd() {

    }

    @FXML
    public void handleUpdate() {
        InventoryModel selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Select an item first");
            return;
        }

        try {
            if (InventoryDTO.update(
                    selected.getInventoryId(),
                    txtItemName.getText(),
                    Integer.parseInt(txtQuantity.getText()),
                    txtUnit.getText()
            )) {
                refreshTable();
                clearFields();
            }
        } catch (Exception e) {
            showError("Update failed");
        }
    }

    @FXML
    public void handleDelete() {
        InventoryModel selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Select an item first");
            return;
        }

        try {
            if (InventoryDTO.delete(selected.getInventoryId())) {
                refreshTable();
            }
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    public void handleSearch(KeyEvent event) {
        try {
            String text = searchField.getText().trim();
            List<InventoryModel> list =
                    text.isEmpty()
                            ? InventoryDTO.getAll()
                            : InventoryDTO.search(text);

            inventoryTable.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    private void clearFields() {
        txtItemName.clear();
        txtQuantity.clear();
        txtUnit.clear();
    }


//   private void openForm(InventoryModel inventory) {
//    try {
//        FXMLLoader loader = new FXMLLoader(
//            getClass().getResource("/com/chethana/restaurant_management_system/inventory.fxml")
//        );
//
//        Stage stage = new Stage();
//        stage.setScene(new Scene(loader.load()));
//        stage.initModality(Modality.APPLICATION_MODAL);
//
//        InventoryController controller = loader.getController();
//        controller.setInventoryController(this);
//
//        if (inventory != null) {
//            controller.setEditMode(inventory);
//        }
//
//        stage.showAndWait();
//    } catch (Exception e) {
//        showError(e.getMessage());
//    }
//}



    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    public void handleAddItem(ActionEvent actionEvent) {
    }

    public void handleEditItem(ActionEvent actionEvent) {
    }

    public void handleUpdateStock(ActionEvent actionEvent) {
    }

    public void handleDeleteItem(ActionEvent actionEvent) {
    }
}


*/
