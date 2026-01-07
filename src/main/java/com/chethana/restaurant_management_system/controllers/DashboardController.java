package com.chethana.restaurant_management_system.controllers;

import com.chethana.restaurant_management_system.Launcher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardController {

    @FXML private VBox sidebarContainer;
    @FXML private Label welcomeLabel;
    @FXML private Label userRoleLabel;
    @FXML private Label dateLabel;
    @FXML private Label timeLabel;
    @FXML private StackPane contentPane;

    // Navigation buttons
    @FXML private Button dashboardBtn;
    @FXML private Button ordersBtn;
    @FXML private Button reservationsBtn;
    @FXML private Button menuBtn;
    @FXML private Button customersBtn;
    @FXML private Button staffBtn;
    @FXML private Button inventoryBtn;
    @FXML private Button paymentsBtn;
    @FXML private Button notificationsBtn;
    @FXML private Button settingsBtn;

    private Button currentActiveButton;

    @FXML
    public void initialize() {
        setupUserInfo();
        setupDateTime();
        setupNavigation();
        setupButtonHoverEffects();
        showDashboard();
    }

    private void setupUserInfo() {
        welcomeLabel.setText("M.A Jayasinghe");
        userRoleLabel.setText("ADMINISTRATOR");
    }

    private void setupDateTime() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        Thread updateTimeThread = new Thread(() -> {
            while (true) {
                try {
                    LocalDateTime now = LocalDateTime.now();
                    String formattedDate = now.format(dateFormatter);
                    String formattedTime = now.format(timeFormatter);

                    javafx.application.Platform.runLater(() -> {
                        if (dateLabel != null) {
                            dateLabel.setText(formattedDate);
                        }
                        if (timeLabel != null) {
                            timeLabel.setText(formattedTime);
                        }
                    });

                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        updateTimeThread.setDaemon(true);
        updateTimeThread.start();
    }

    private void setupNavigation() {
        dashboardBtn.setOnAction(e -> showDashboard());
        ordersBtn.setOnAction(e -> showOrderManagement());
        reservationsBtn.setOnAction(e -> showReservationManagement());
        menuBtn.setOnAction(e -> showMenuManagement());
        customersBtn.setOnAction(e -> showCustomerManagement());
        staffBtn.setOnAction(e -> showStaffManagement());
        inventoryBtn.setOnAction(e -> showInventoryManagement());
        paymentsBtn.setOnAction(e -> showPaymentManagement());
        notificationsBtn.setOnAction(e -> showNotifications());
        settingsBtn.setOnAction(e -> showSettings());
    }

    private void setupButtonHoverEffects() {
        Button[] buttons = {dashboardBtn, ordersBtn, reservationsBtn, menuBtn,
                customersBtn, staffBtn, inventoryBtn, paymentsBtn,
                notificationsBtn, settingsBtn};

        for (Button button : buttons) {
            if (button != null) {
                button.setOnMouseEntered(this::onButtonHover);
                button.setOnMouseExited(this::onButtonExit);
            }
        }
    }

    @FXML
    private void onButtonHover(MouseEvent event) {
        Button button = (Button) event.getSource();
        if (button != currentActiveButton) {
            button.setStyle(button.getStyle().replace("transparent", "#eff6ff"));
        }
    }

    @FXML
    private void onButtonExit(MouseEvent event) {
        Button button = (Button) event.getSource();
        if (button != currentActiveButton) {
            button.setStyle(button.getStyle().replace("#eff6ff", "transparent"));
        }
    }

    private void setActiveButton(Button activeButton) {
        Button[] allButtons = {dashboardBtn, ordersBtn, reservationsBtn, menuBtn,
                customersBtn, staffBtn, inventoryBtn, paymentsBtn,
                notificationsBtn, settingsBtn};

        // Reset all buttons to inactive state
        for (Button button : allButtons) {
            if (button != null) {
                button.setStyle("-fx-background-color: transparent; -fx-text-fill: #475569; " +
                        "-fx-font-size: 14px; -fx-font-weight: 500; -fx-padding: 12 20; " +
                        "-fx-background-radius: 8; -fx-cursor: hand; -fx-alignment: CENTER_LEFT; -fx-border-width: 0;");
            }
        }

        // Set active button style
        if (activeButton != null) {
            activeButton.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; " +
                    "-fx-font-size: 14px; -fx-font-weight: 600; -fx-padding: 12 20; " +
                    "-fx-background-radius: 8; -fx-cursor: hand; -fx-alignment: CENTER_LEFT; -fx-border-width: 0;");
            currentActiveButton = activeButton;
        }
    }

    @FXML
    private void showDashboard() {
        setActiveButton(dashboardBtn);
        loadContent("dashboard-content.fxml");
    }

    @FXML
    private void showOrderManagement() {
        setActiveButton(ordersBtn);
        loadContent("orders.fxml");
    }

    @FXML
    private void showReservationManagement() {
        setActiveButton(reservationsBtn);
        loadContent("reservations.fxml");
    }

    @FXML
    private void showMenuManagement() {
        setActiveButton(menuBtn);
        loadContent("menu.fxml");
    }

    @FXML
    private void showCustomerManagement() {
        setActiveButton(customersBtn);
        loadContent("customer.fxml");
    }

    @FXML
    private void showStaffManagement() {
        setActiveButton(staffBtn);
        loadContent("staff.fxml");
    }

    @FXML
    private void showInventoryManagement() {
        setActiveButton(inventoryBtn);
        loadContent("inventory.fxml");
    }

    @FXML
    private void showPaymentManagement() {
        setActiveButton(paymentsBtn);
        loadContent("payments.fxml");
    }

    @FXML
    private void showNotifications() {
        setActiveButton(notificationsBtn);
        loadContent("notifications.fxml");
    }

    @FXML
    private void showSettings() {
        setActiveButton(settingsBtn);
        loadContent("settings.fxml");
    }

    private void loadContent(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(Launcher.class.getResource(fxmlFile));
            Parent content = loader.load();
            contentPane.getChildren().setAll(content);
        } catch (IOException e) {
            // If the FXML file doesn't exist, show a placeholder
            Label placeholder = new Label("Content for " + fxmlFile + " coming soon...");
            placeholder.setStyle("-fx-font-size: 18px; -fx-text-fill: #64748b;");
            contentPane.getChildren().setAll(placeholder);
        }
    }

    @FXML
    private void handleLogout() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout");
            alert.setHeaderText("Confirm Logout");
            alert.setContentText("Are you sure you want to logout?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("login.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) sidebarContainer.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Restaurant Management System - Login");
                stage.setMaximized(false);
                stage.centerOnScreen();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error during logout");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}