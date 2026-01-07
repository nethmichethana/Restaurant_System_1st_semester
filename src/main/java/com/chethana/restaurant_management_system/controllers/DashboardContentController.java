package com.chethana.restaurant_management_system.controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class DashboardContentController {

    // Header & Footer
    @FXML
    private Label dateLabel;
    @FXML
    private Label lastUpdatedLabel;

    // Stats Cards
    @FXML
    private Label totalOrdersLabel;
    @FXML
    private Label ordersTodayLabel;
    @FXML
    private Label totalRevenueLabel;
    @FXML
    private Label revenueTodayLabel;
    @FXML
    private Label totalCustomersLabel;
    @FXML
    private Label pendingOrdersLabel;
    @FXML
    private Label alertCountLabel;
    @FXML
    private Label reservationCountLabel;

    // Charts
    @FXML
    private LineChart<String, Number> revenueChart;
    @FXML
    private PieChart orderStatusChart;

    // Inventory Alerts
    @FXML
    private ListView<String> inventoryAlertsList;

    // Tables
    @FXML
    private TableView<?> recentOrdersTable;
    @FXML
    private TableColumn<?, ?> colRecentOrderId;
    @FXML
    private TableColumn<?, ?> colRecentCustomer;
    @FXML
    private TableColumn<?, ?> colRecentAmount;
    @FXML
    private TableColumn<?, ?> colRecentStatus;
    @FXML
    private TableColumn<?, ?> colRecentTime;

    @FXML
    private TableView<?> todayReservationsTable;
    @FXML
    private TableColumn<?, ?> colReservationCustomer;
    @FXML
    private TableColumn<?, ?> colReservationTime;
    @FXML
    private TableColumn<?, ?> colReservationGuests;
    @FXML
    private TableColumn<?, ?> colReservationStatus;

    @FXML
    public void initialize() {
        // Initialize date label
        dateLabel.setText(java.time.LocalDate.now().toString());

        // Initialize stats with dummy data
        totalOrdersLabel.setText("124");
        ordersTodayLabel.setText("Today: 12");
        totalRevenueLabel.setText("Rs. 54,320.00");
        revenueTodayLabel.setText("Today: Rs. 4,120.00");
        totalCustomersLabel.setText("89");
        pendingOrdersLabel.setText("5");
        alertCountLabel.setText("2 items");
        reservationCountLabel.setText("7 reservations");
        lastUpdatedLabel.setText("Just now");

        // Initialize PieChart with dummy data
        orderStatusChart.getData().addAll(
                new PieChart.Data("Completed", 80),
                new PieChart.Data("Pending", 15),
                new PieChart.Data("Cancelled", 5)
        );

        // Optionally initialize LineChart (RevenueChart) with dummy data
        var series = new javafx.scene.chart.XYChart.Series<String, Number>();
        series.setName("Revenue");
        series.getData().add(new javafx.scene.chart.XYChart.Data<>("Mon", 5000));
        series.getData().add(new javafx.scene.chart.XYChart.Data<>("Tue", 7000));
        series.getData().add(new javafx.scene.chart.XYChart.Data<>("Wed", 6000));
        series.getData().add(new javafx.scene.chart.XYChart.Data<>("Thu", 8000));
        series.getData().add(new javafx.scene.chart.XYChart.Data<>("Fri", 5500));
        series.getData().add(new javafx.scene.chart.XYChart.Data<>("Sat", 9000));
        series.getData().add(new javafx.scene.chart.XYChart.Data<>("Sun", 7500));
        revenueChart.getData().add(series);

        // Dummy inventory alerts
        inventoryAlertsList.getItems().addAll("Tomatoes - Low", "Cheese - Low");
    }
}
