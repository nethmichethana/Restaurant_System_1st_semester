package com.chethana.restaurant_management_system.controllers;

import com.chethana.restaurant_management_system.model.paymentModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {

    @FXML private TableView<paymentModel> tblPayments;
    @FXML private TableColumn<paymentModel, Integer> colPaymentId;
    @FXML private TableColumn<paymentModel, Integer> colOrderId;
    @FXML private TableColumn<paymentModel, String> colCustomer;
    @FXML private TableColumn<paymentModel, String> colMethod;
    @FXML private TableColumn<paymentModel, Double> colAmount;
    @FXML private TableColumn<paymentModel, LocalDate> colPaymentDate;

    @FXML private DatePicker txtFromDate;
    @FXML private DatePicker txtToDate;

    private final ObservableList<paymentModel> paymentList =
            FXCollections.observableArrayList();

    // ---------------- INITIALIZE ----------------
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
        loadAllPayments();
    }

    private void setupTable() {
        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colPaymentDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));

        tblPayments.setItems(paymentList);
    }

    // ---------------- LOAD DATA ----------------
    private void loadAllPayments() {
        paymentList.clear();

        paymentList.add(new paymentModel(
                1, 1001, "Nimal", "Cash", 2500.00, LocalDate.now()
        ));
        paymentList.add(new paymentModel(
                2, 1002, "Kamal", "Card", 4200.00, LocalDate.now().minusDays(1)
        ));
        paymentList.add(new paymentModel(
                3, 1003, "Saman", "UPI", 1800.00, LocalDate.now().minusDays(2)
        ));
    }

    // ---------------- ADD PAYMENT ----------------
    @FXML
    private void handleAddPayment(ActionEvent event) {
        int newId = paymentList.size() + 1;

        paymentList.add(new paymentModel(
                newId,
                1000 + newId,
                "New Customer",
                "Cash",
                3000.00,
                LocalDate.now()
        ));

        new Alert(Alert.AlertType.INFORMATION,
                "New payment added successfully!").show();
    }

    // ---------------- VIEW PAYMENT ----------------
    @FXML
    private void handleViewPayment(ActionEvent event) {
        paymentModel selected = tblPayments.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Please select a payment first");
            return;
        }

        new Alert(Alert.AlertType.INFORMATION,
                "Order ID: " + selected.getOrderId() +
                        "\nCustomer: " + selected.getCustomerName() +
                        "\nMethod: " + selected.getPaymentMethod() +
                        "\nAmount: Rs. " + selected.getAmount() +
                        "\nDate: " + selected.getPaymentDate()
        ).showAndWait();
    }

    // ---------------- SEARCH ----------------
    @FXML
    private void handleSearch(ActionEvent event) {
        LocalDate from = txtFromDate.getValue();
        LocalDate to = txtToDate.getValue();

        if (from == null || to == null) {
            showWarning("Please select both dates");
            return;
        }

        ObservableList<paymentModel> filtered =
                FXCollections.observableArrayList();

        for (paymentModel p : paymentList) {
            if (!p.getPaymentDate().isBefore(from)
                    && !p.getPaymentDate().isAfter(to)) {
                filtered.add(p);
            }
        }

        tblPayments.setItems(filtered);
    }

    // ---------------- REFRESH ----------------
    public void refreshTable() {
        tblPayments.setItems(paymentList);
    }

    private void showWarning(String msg) {
        new Alert(Alert.AlertType.WARNING, msg).show();
    }
}
