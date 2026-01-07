package com.chethana.restaurant_management_system.controllers;

import com.chethana.restaurant_management_system.dto.OrderDTO;
import com.chethana.restaurant_management_system.model.OrderModel;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderController {
    @FXML private TableView<OrderModel> orderTable;
    @FXML private TableColumn<OrderModel, Integer> colOrderId;
    @FXML private TableColumn<OrderModel, String> colCustomerName;
    @FXML private TableColumn<OrderModel, String> colStaffName;
    @FXML private TableColumn<OrderModel, String> colOrderTime;
    @FXML private TableColumn<OrderModel, BigDecimal> colTotalAmount;
    @FXML private TableColumn<OrderModel, String> colStatus;

    @FXML private TextField searchField;
    @FXML private DatePicker dateFilter;
    @FXML private ComboBox<String> statusFilter;

    @FXML private Label totalOrdersLabel;
    @FXML private Label completedOrdersLabel;
    @FXML private Label pendingOrdersLabel;
    @FXML private Label revenueLabel;
    @FXML private Label statusLabel;
    @FXML private Label recordCountLabel;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    public void initialize() {
        colOrderId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getOrderId()).asObject());
        colCustomerName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerName()));
        colStaffName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStaffName()));
        colOrderTime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderTime().format(dateTimeFormatter)));
        colTotalAmount.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTotalAmount()));
        colStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        orderTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        setupStatusFilter();
        refreshTable();
        updateStats();
    }

    private void setupStatusFilter() {
        try {
            statusFilter.getItems().clear();
            statusFilter.getItems().add("All Status");
            List<String> statuses = OrderDTO.getDistinctStatus();
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
            List<OrderModel> list = OrderDTO.getAllOrders();
            orderTable.setItems(FXCollections.observableArrayList(list));
            updateRecordCount(list.size());
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    public void updateStats() {
        try {
            int total = OrderDTO.getTotalOrdersCount();
            int completed = OrderDTO.getOrdersCountByStatus("Completed");
            int pending = OrderDTO.getOrdersCountByStatus("Pending");
            BigDecimal revenue = OrderDTO.getTodayRevenue();

            totalOrdersLabel.setText(String.valueOf(total));
            completedOrdersLabel.setText(String.valueOf(completed));
            pendingOrdersLabel.setText(String.valueOf(pending));
            revenueLabel.setText("Rs. " + revenue.toString());
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    private void updateRecordCount(int count) {
        recordCountLabel.setText("Showing " + count + " orders");
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

            List<OrderModel> list;

            if (date != null && !"All Status".equals(status)) {
                list = getOrdersByDateAndStatus(date, status);
            } else if (date != null) {
                list = OrderDTO.getOrdersByDate(date);
            } else if (!"All Status".equals(status)) {
                list = OrderDTO.getOrdersByStatus(status);
            } else if (!search.isEmpty()) {
                list = OrderDTO.searchOrders(search);
            } else {
                list = OrderDTO.getAllOrders();
            }

            orderTable.setItems(FXCollections.observableArrayList(list));
            updateRecordCount(list.size());
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    private List<OrderModel> getOrdersByDateAndStatus(LocalDate date, String status) throws SQLException {
        String sql = "SELECT o.*, c.name as customer_name, s.name as staff_name FROM orders o " +
                "LEFT JOIN customer c ON o.customer_id = c.customer_id " +
                "LEFT JOIN staff s ON o.staff_id = s.staff_id " +
                "WHERE DATE(o.order_time) = ? AND o.status=? " +
                "ORDER BY o.order_time DESC";
        java.sql.ResultSet rs = com.chethana.restaurant_management_system.util.CrudUtil.executeQuery(sql, date, status);
        List<OrderModel> list = new java.util.ArrayList<>();
        while (rs.next()) {
            OrderModel order = new OrderModel();
            order.setOrderId(rs.getInt("order_id"));
            order.setOrderCode(rs.getString("order_code"));
            order.setCustomerId(rs.getInt("customer_id"));
            order.setCustomerName(rs.getString("customer_name"));
            order.setStaffId(rs.getInt("staff_id"));
            order.setStaffName(rs.getString("staff_name"));
            order.setOrderTime(rs.getTimestamp("order_time").toLocalDateTime());
            order.setTotalAmount(rs.getBigDecimal("total_amount"));
            order.setStatus(rs.getString("status"));
            order.setPaymentMethod(rs.getString("payment_method"));
            order.setNotes(rs.getString("notes"));
            list.add(order);
        }
        return list;
    }

    @FXML
    public void handleNewOrder() {
        openForm();
    }

    @FXML
    public void handleViewOrder() {
        OrderModel selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select an order");
            return;
        }
        viewOrderDetails(selected);
    }

    @FXML
    public void handleUpdateStatus() {
        OrderModel selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select an order");
            return;
        }
        updateOrderStatus(selected);
    }

    @FXML
    public void handleCancelOrder() {
        OrderModel selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select an order");
            return;
        }

        if ("Completed".equals(selected.getStatus()) || "Cancelled".equals(selected.getStatus())) {
            showError("Cannot cancel a " + selected.getStatus() + " order");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Cancel");
        confirm.setHeaderText("Cancel Order");
        confirm.setContentText("Are you sure you want to cancel order #" + selected.getOrderId() + "?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    if (OrderDTO.updateOrderStatus(selected.getOrderId(), "Cancelled")) {
                        refreshTable();
                        updateStats();
                        statusLabel.setText("Order cancelled successfully");
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

    private void openForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/chethana/restaurant_management_system/order_form.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            OrderFormController controller = loader.getController();
            controller.setOrderController(this);
            stage.showAndWait();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void viewOrderDetails(OrderModel order) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/chethana/restaurant_management_system/order_details.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            OrderDetailsController controller = loader.getController();
            controller.setOrder(order);
            stage.showAndWait();
        } catch (Exception e) {
            showError("Failed to load order details: " + e.getMessage());
        }
    }

    private void updateOrderStatus(OrderModel order) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Update Order Status");
        dialog.setHeaderText("Update status for Order #" + order.getOrderId());

        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll("Pending", "Preparing", "Ready", "Served", "Completed", "Cancelled");
        statusCombo.setValue(order.getStatus());

        VBox content = new VBox(10);
        content.getChildren().addAll(new Label("Select new status:"), statusCombo);
        dialog.getDialogPane().setContent(content);

        ButtonType updateButton = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButton, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButton) {
                return statusCombo.getValue();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(newStatus -> {
            if (newStatus != null && !newStatus.equals(order.getStatus())) {
                try {
                    if (OrderDTO.updateOrderStatus(order.getOrderId(), newStatus)) {
                        refreshTable();
                        updateStats();
                        statusLabel.setText("Order status updated to: " + newStatus);
                    }
                } catch (SQLException e) {
                    showError(e.getMessage());
                }
            }
        });
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    public void setStatus(String message) {
        statusLabel.setText(message);
    }
}