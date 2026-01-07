package com.chethana.restaurant_management_system.controllers;

import com.chethana.restaurant_management_system.Launcher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private VBox brandingSection;

    @FXML
    public void initialize() {
        setupFieldFocusEffects();
        setupButtonHoverEffect();
        setupResponsiveLayout();

        // Add enter key support for fields
        usernameField.setOnAction(event -> passwordField.requestFocus());
        passwordField.setOnAction(event -> handleLogin());
    }

    private void setupFieldFocusEffects() {
        // Username field focus effects
        usernameField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                usernameField.setStyle("-fx-padding: 14 16; -fx-font-size: 14px; " +
                        "-fx-background-color: white; -fx-text-fill: #1e293b; " +
                        "-fx-prompt-text-fill: #94a3b8; -fx-background-radius: 10; " +
                        "-fx-border-color: #3b82f6; -fx-border-width: 2; -fx-border-radius: 10;");
            } else {
                usernameField.setStyle("-fx-padding: 14 16; -fx-font-size: 14px; " +
                        "-fx-background-color: #f8fafc; -fx-text-fill: #1e293b; " +
                        "-fx-prompt-text-fill: #94a3b8; -fx-background-radius: 10; " +
                        "-fx-border-color: #cbd5e1; -fx-border-width: 1.5; -fx-border-radius: 10;");
            }
        });

        // Password field focus effects
        passwordField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                passwordField.setStyle("-fx-padding: 14 16; -fx-font-size: 14px; " +
                        "-fx-background-color: white; -fx-text-fill: #1e293b; " +
                        "-fx-prompt-text-fill: #94a3b8; -fx-background-radius: 10; " +
                        "-fx-border-color: #3b82f6; -fx-border-width: 2; -fx-border-radius: 10;");
            } else {
                passwordField.setStyle("-fx-padding: 14 16; -fx-font-size: 14px; " +
                        "-fx-background-color: #f8fafc; -fx-text-fill: #1e293b; " +
                        "-fx-prompt-text-fill: #94a3b8; -fx-background-radius: 10; " +
                        "-fx-border-color: #cbd5e1; -fx-border-width: 1.5; -fx-border-radius: 10;");
            }
        });
    }

    private void setupButtonHoverEffect() {
        loginButton.setOnMouseEntered(e -> {
            loginButton.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white; " +
                    "-fx-font-size: 15px; -fx-font-weight: bold; -fx-padding: 16; " +
                    "-fx-background-radius: 10; -fx-cursor: hand; -fx-border-width: 0;");

            ScaleTransition st = new ScaleTransition(Duration.millis(100), loginButton);
            st.setToX(1.02);
            st.setToY(1.02);
            st.play();
        });

        loginButton.setOnMouseExited(e -> {
            loginButton.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; " +
                    "-fx-font-size: 15px; -fx-font-weight: bold; -fx-padding: 16; " +
                    "-fx-background-radius: 10; -fx-cursor: hand; -fx-border-width: 0;");

            ScaleTransition st = new ScaleTransition(Duration.millis(100), loginButton);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
    }

    private void setupResponsiveLayout() {
        // Listen for window size changes to hide/show branding section
        if (brandingSection != null) {
            brandingSection.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    newScene.widthProperty().addListener((obsWidth, oldWidth, newWidth) -> {
                        if (newWidth.doubleValue() < 900) {
                            brandingSection.setVisible(false);
                            brandingSection.setManaged(false);
                        } else {
                            brandingSection.setVisible(true);
                            brandingSection.setManaged(true);
                        }
                    });
                }
            });
        }
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please enter both username and password.");
            return;
        }

        // Simple validation (replace with actual authentication)
        if (validateCredentials(username, password)) {
            loginSuccess();
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed",
                    "Invalid username or password. Please try again.");
            passwordField.clear();
            usernameField.requestFocus();
        }
    }

    @FXML
    private void quickLoginAdmin() {
        usernameField.setText("admin");
        passwordField.setText("admin123");
        handleLogin();
    }

    @FXML
    private void quickLoginStaff() {
        usernameField.setText("staff");
        passwordField.setText("staff123");
        handleLogin();
    }

    @FXML
    private void quickLoginGuest() {
        usernameField.setText("guest");
        passwordField.setText("guest123");
        handleLogin();
    }

    private boolean validateCredentials(String username, String password) {
        // Replace this with actual database authentication
        return (username.equals("admin") && password.equals("admin123")) ||
                (username.equals("staff") && password.equals("staff123")) ||
                (username.equals("guest") && password.equals("guest123"));
    }

    private void loginSuccess() {
        try {
            // Add fade out animation
            FadeTransition ft = new FadeTransition(Duration.millis(300),
                    usernameField.getScene().getRoot());
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.setOnFinished(event -> {
                try {
                    loadDashboard();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error",
                            "Failed to load dashboard.");
                }
            });
            ft.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDashboard() throws IOException {
        FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("dashboard.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) loginButton.getScene().getWindow();
        Scene scene = new Scene(root);

        // Add fade in animation
        root.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(400), root);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();

        stage.setScene(scene);
        stage.setTitle("Restaurant Management System - Dashboard");
        stage.setMaximized(true);
        stage.show();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        // Style the alert to match the theme
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: white;");

        alert.showAndWait();
    }
}