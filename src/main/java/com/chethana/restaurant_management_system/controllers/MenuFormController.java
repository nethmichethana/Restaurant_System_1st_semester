package com.chethana.restaurant_management_system.controllers;

import com.chethana.restaurant_management_system.dto.MenuDTO;
import com.chethana.restaurant_management_system.model.MenuModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.SQLException;

public class MenuFormController {
    @FXML private TextField nameField;
    @FXML private TextField categoryField;
    @FXML private TextArea descriptionField;
    @FXML private TextField priceField;
    @FXML private CheckBox availabilityCheck;
    private MenuController menuController;
    private boolean editMode = false;
    private int menuId;

    public void setMenuController(MenuController controller) {
        this.menuController = controller;
    }

    public void setEditMode(MenuModel menu) {
        editMode = true;
        menuId = menu.getMenuItemId();
        nameField.setText(menu.getName());
        categoryField.setText(menu.getCategory());
        descriptionField.setText(menu.getDescription());
        priceField.setText(menu.getPrice().toString());
        availabilityCheck.setSelected(menu.getAvailability());
    }

    @FXML
    public void handleSave() {
        if (!validate()) return;
        MenuModel menu = new MenuModel();
        menu.setName(nameField.getText().trim());
        menu.setCategory(categoryField.getText().trim());
        menu.setDescription(descriptionField.getText().trim());
        menu.setPrice(new BigDecimal(priceField.getText().trim()));
        menu.setAvailability(availabilityCheck.isSelected());
        try {
            boolean success;
            if (editMode) {
                menu.setMenuItemId(menuId);
                success = MenuDTO.updateMenu(menu);
            } else {
                success = MenuDTO.saveMenu(menu);
            }
            if (success) {
                menuController.refreshTable();
                close();
            }
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    public void handleCancel() {
        close();
    }

    private boolean validate() {
        if (nameField.getText().isEmpty()) return error("Name required");
        if (categoryField.getText().isEmpty()) return error("Category required");
        try {
            new BigDecimal(priceField.getText());
        } catch (Exception e) {
            return error("Invalid price");
        }
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
        ((Stage) nameField.getScene().getWindow()).close();
    }
}