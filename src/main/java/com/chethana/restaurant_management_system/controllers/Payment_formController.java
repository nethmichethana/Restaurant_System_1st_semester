package com.chethana.restaurant_management_system.controllers;

import com.chethana.restaurant_management_system.model.paymentModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class Payment_formController implements Initializable {
    public Button saveButton;
    public Button cancelButton;



    @FXML
    private TextField txtPaymentId;

    @FXML
    private TextField txtOrderId;

    @FXML
    private ComboBox<String> cmbCustomer;

    @FXML
    private ComboBox<String> cmbMethod;

    @FXML
    private TextField txtAmount;

    @FXML
    private DatePicker dpPaymentDate;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCustomers();
        loadPaymentMethods();
        dpPaymentDate.setValue(LocalDate.now());
    }


    private void loadCustomers() {
        cmbCustomer.getItems().addAll(
                "Nimal",
                "Kamal",
                "Saman",
                "Amal"
        );
    }

    private void loadPaymentMethods() {
        cmbMethod.getItems().addAll(
                "Cash",
                "Card",
                "UPI",
                "Bank Transfer"
        );
    }


    @FXML
    private void handleAddPayment(ActionEvent event) {

        // Validation
        if (txtPaymentId.getText().isEmpty()
                || txtOrderId.getText().isEmpty()
                || cmbCustomer.getValue() == null
                || cmbMethod.getValue() == null
                || txtAmount.getText().isEmpty()
                || dpPaymentDate.getValue() == null) {

            showAlert(Alert.AlertType.WARNING,
                    "Validation Error",
                    "Please fill all required fields");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(txtAmount.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR,
                    "Invalid Amount",
                    "Please enter a valid amount");
            return;
        }

        paymentModel payment = new paymentModel(
                0, // ID can be auto-generated later
                Integer.parseInt(txtOrderId.getText().replaceAll("\\D", "")),
                cmbCustomer.getValue(),
                cmbMethod.getValue(),
                amount,
                dpPaymentDate.getValue()
        );


        showAlert(Alert.AlertType.INFORMATION,
                "Payment Added",
                "Payment added successfully!");

        clearForm();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) txtPaymentId.getScene().getWindow();
        stage.close();
    }


    private void clearForm() {
        txtPaymentId.clear();
        txtOrderId.clear();
        txtAmount.clear();
        cmbCustomer.getSelectionModel().clearSelection();
        cmbMethod.getSelectionModel().clearSelection();
        dpPaymentDate.setValue(LocalDate.now());
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
