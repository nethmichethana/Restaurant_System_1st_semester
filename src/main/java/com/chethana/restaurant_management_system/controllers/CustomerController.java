package com.chethana.restaurant_management_system.controllers;

import com.chethana.restaurant_management_system.dto.CustomerDTO;
import com.chethana.restaurant_management_system.model.CustomerModel;
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
import java.util.List;
import javafx.event.ActionEvent;

public class CustomerController {
    @FXML private TableView<CustomerModel> customerTable;
    @FXML private TableColumn<CustomerModel, Integer> colCustomerId;
    @FXML private TableColumn<CustomerModel, String> colCustomerCode;
    @FXML private TableColumn<CustomerModel, String> colName;
    @FXML private TableColumn<CustomerModel, String> colEmail;
    @FXML private TableColumn<CustomerModel, String> colContact;
    @FXML private TextField searchField;
    
    CustomerDTO cutomerDTO = new CustomerDTO();
    
    @FXML
    public void initialize() {
        colCustomerId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCustomerId()).asObject());
        colCustomerCode.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerCode()));
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        colEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        colContact.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContact()));

        customerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        refreshTable();
    }

    public void refreshTable() {
        try {
            List<CustomerModel> list = CustomerDTO.getAllCustomers();
            customerTable.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    public void handleSearch(KeyEvent event) {
        filterTable();
    }

    private void filterTable() {
        try {
            String search = searchField.getText().trim();
            List<CustomerModel> list;
            if (search.isEmpty()) {
                list = CustomerDTO.getAllCustomers();
            } else {
                list = CustomerDTO.searchCustomers(search);
            }
            customerTable.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    public void handleAddCustomer() {
        openForm(null);
    }

    @FXML
    public void handleEditCustomer() {
        CustomerModel selected = customerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a customer");
            return;
        }
        openForm(selected);
    }

    @FXML
    public void handleDeleteCustomer() {
        CustomerModel selected = customerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a customer");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Customer");
        confirm.setContentText("Are you sure you want to delete " + selected.getName() + "?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    if (CustomerDTO.deleteCustomer(selected.getCustomerId())) {
                        refreshTable();
                    }
                } catch (SQLException e) {
                    showError(e.getMessage());
                }
            }
        });
    }

    private void openForm(CustomerModel customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/chethana/restaurant_management_system/customer_form.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            CustomerFormController controller = loader.getController();
            controller.setCustomerController(this);
            if (customer != null) controller.setEditMode(customer);
            stage.showAndWait();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    @FXML
    public void handlePrint(ActionEvent event) {
        try{
            cutomerDTO.printCustomerReport();
        }catch (Exception e){}
    }
}