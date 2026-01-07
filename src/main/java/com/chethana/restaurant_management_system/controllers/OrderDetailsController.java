package com.chethana.restaurant_management_system.controllers;

import com.chethana.restaurant_management_system.dto.OrderDTO;
import com.chethana.restaurant_management_system.model.OrderItemModel;
import com.chethana.restaurant_management_system.model.OrderModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.SQLException;

public class OrderDetailsController {
    @FXML private Label orderIdLabel;
    @FXML private Label customerNameLabel;
    @FXML private Label staffNameLabel;
    @FXML private Label orderTimeLabel;
    @FXML private Label totalAmountLabel;
    @FXML private Label statusLabel;
    @FXML private Label paymentMethodLabel;

    @FXML private TableView<OrderItemModel> itemsTable;
    @FXML private TableColumn<OrderItemModel, String> colItemName;
    @FXML private TableColumn<OrderItemModel, Integer> colQuantity;
    @FXML private TableColumn<OrderItemModel, BigDecimal> colUnitPrice;
    @FXML private TableColumn<OrderItemModel, BigDecimal> colTotalPrice;

    private OrderModel order;

    @FXML
    public void initialize() {
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
    }

    public void setOrder(OrderModel order) {
        this.order = order;
        loadOrderDetails();
        loadOrderItems();
    }

    private void loadOrderDetails() {
        orderIdLabel.setText(String.valueOf(order.getOrderId()));
        customerNameLabel.setText(order.getCustomerName());
        staffNameLabel.setText(order.getStaffName());
        orderTimeLabel.setText(order.getOrderTime().toString());
        totalAmountLabel.setText("Rs. " + order.getTotalAmount().toString());
        statusLabel.setText(order.getStatus());
        paymentMethodLabel.setText(order.getPaymentMethod() != null ? order.getPaymentMethod() : "N/A");
    }

    private void loadOrderItems() {
        try {
            itemsTable.getItems().clear();
            itemsTable.getItems().addAll(OrderDTO.getOrderItems(order.getOrderId()));
        } catch (SQLException e) {
            showError("Failed to load order items: " + e.getMessage());
        }
    }

    @FXML
    public void handleClose() {
        ((Stage) orderIdLabel.getScene().getWindow()).close();
    }

    private void showError(String msg) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}