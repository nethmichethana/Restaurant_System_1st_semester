package com.chethana.restaurant_management_system.controllers;

import com.chethana.restaurant_management_system.dto.OrderDTO;
import com.chethana.restaurant_management_system.model.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderFormController {

    @FXML private ComboBox<CustomerModel> customerCombo;
    @FXML private ComboBox<StaffModel> waiterCombo;
    @FXML private ComboBox<MenuModel> menuItemCombo;
    @FXML private TextField quantityField;

    @FXML private TableView<OrderItemTempModel> itemsTable;
    @FXML private TableColumn<OrderItemTempModel, String> colItemName;
    @FXML private TableColumn<OrderItemTempModel, Integer> colItemQty;
    @FXML private TableColumn<OrderItemTempModel, BigDecimal> colItemPrice;
    @FXML private TableColumn<OrderItemTempModel, BigDecimal> colItemTotal;
    @FXML private TableColumn<OrderItemTempModel, String> colItemAction;

    @FXML private Label totalAmountLabel;

    private OrderController orderController;
    private final List<OrderItemTempModel> orderItems = new ArrayList<>();
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @FXML
    public void initialize() {
        colItemName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getItemName()));

        colItemQty.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getQuantity()));

        colItemPrice.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getUnitPrice()));

        colItemTotal.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getTotalPrice()));

        colItemAction.setCellFactory(param -> new TableCell<>() {
            private final Button deleteBtn = new Button("❌");

            {
                deleteBtn.setOnAction(event -> {
                    OrderItemTempModel item =
                            getTableView().getItems().get(getIndex());
                    removeItem(item);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });

        loadComboBoxData();
        refreshTable();
        calculateTotal();
    }

    public void setOrderController(OrderController controller) {
        this.orderController = controller;
    }

    private void loadComboBoxData() {
        try {
            customerCombo.setItems(
                    FXCollections.observableArrayList(OrderDTO.getAllCustomersForCombo()));

            waiterCombo.setItems(
                    FXCollections.observableArrayList(OrderDTO.getAllWaitersForCombo()));

            menuItemCombo.setItems(
                    FXCollections.observableArrayList(OrderDTO.getAllMenuItemsForCombo()));

        } catch (SQLException e) {
            showError("Failed to load data : " + e.getMessage());
        }
    }

    @FXML
    public void handleAddItem() {
        if (menuItemCombo.getValue() == null) {
            showError("Please select a menu item");
            return;
        }

        if (quantityField.getText().trim().isEmpty()) {
            showError("Please enter quantity");
            return;
        }

        try {
            int qty = Integer.parseInt(quantityField.getText().trim());
            if (qty <= 0) {
                showError("Quantity must be greater than zero");
                return;
            }

            MenuModel menu = menuItemCombo.getValue();

            for (OrderItemTempModel item : orderItems) {
                if (item.getMenuItemId() == menu.getMenuItemId()) {
                    item.setQuantity(item.getQuantity() + qty);
                    refreshTable();
                    calculateTotal();
                    quantityField.clear();
                    return;
                }
            }

            OrderItemTempModel newItem = new OrderItemTempModel(
                    menu.getMenuItemId(),
                    menu.getName(),
                    qty,
                    menu.getPrice()
            );

            orderItems.add(newItem);
            refreshTable();
            calculateTotal();
            quantityField.clear();

        } catch (NumberFormatException e) {
            showError("Invalid quantity");
        }
    }

    private void removeItem(OrderItemTempModel item) {
        orderItems.remove(item);
        refreshTable();
        calculateTotal();
    }

    private void refreshTable() {
        itemsTable.setItems(FXCollections.observableArrayList(orderItems));
    }

    private void calculateTotal() {
        totalAmount = BigDecimal.ZERO;
        for (OrderItemTempModel item : orderItems) {
            totalAmount = totalAmount.add(item.getTotalPrice());
        }
        totalAmountLabel.setText("Rs. " + totalAmount);
    }

    @FXML
    public void handleSave() {
        if (!validate()) return;

        try {
            OrderModel order = new OrderModel();
            order.setCustomerId(customerCombo.getValue().getCustomerId());
            order.setStaffId(waiterCombo.getValue().getStaffId());
            order.setTotalAmount(totalAmount);
            order.setStatus("Pending");
            order.setPaymentMethod("Cash");
            order.setNotes("");

            List<OrderItemModel> orderItemList = new ArrayList<>();
            for (OrderItemTempModel temp : orderItems) {
                OrderItemModel item = new OrderItemModel();
                item.setMenuItemId(temp.getMenuItemId());
                item.setQuantity(temp.getQuantity());
                item.setUnitPrice(temp.getUnitPrice());
                item.setTotalPrice(temp.getTotalPrice());
                orderItemList.add(item);
            }

            boolean success = OrderDTO.saveOrder(order, orderItemList);
            if (success) {
                orderController.refreshTable();
                orderController.updateStats();
                orderController.setStatus("Order placed successfully");
                close();
            }

        } catch (SQLException e) {
            showError("Database error : " + e.getMessage());
        }
    }

    @FXML
    public void handleCancel() {
        close();
    }

    private boolean validate() {
        if (customerCombo.getValue() == null)
            return error("Please select a customer");

        if (waiterCombo.getValue() == null)
            return error("Please select a waiter");

        if (orderItems.isEmpty())
            return error("Please add at least one menu item");

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
        Stage stage = (Stage) customerCombo.getScene().getWindow();
        stage.close();
    }
}
