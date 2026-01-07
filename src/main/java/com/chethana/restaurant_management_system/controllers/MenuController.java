package com.chethana.restaurant_management_system.controllers;

import com.chethana.restaurant_management_system.dto.MenuDTO;
import com.chethana.restaurant_management_system.model.MenuModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
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

public class MenuController {
    @FXML private TableView<MenuModel> menuTable;
    @FXML private TableColumn<MenuModel, Integer> colMenuItemId;
    @FXML private TableColumn<MenuModel, String> colName;
    @FXML private TableColumn<MenuModel, String> colCategory;
    @FXML private TableColumn<MenuModel, String> colDescription;
    @FXML private TableColumn<MenuModel, java.math.BigDecimal> colPrice;
    @FXML private TableColumn<MenuModel, Boolean> colAvailability;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> categoryFilter;

    @FXML
    public void initialize() {
        colMenuItemId.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getMenuItemId()).asObject());
        colName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));
        colCategory.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCategory()));
        colDescription.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescription()));
        colPrice.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getPrice()));
        colAvailability.setCellValueFactory(cellData ->
                new SimpleBooleanProperty(cellData.getValue().getAvailability()).asObject());

        colAvailability.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : (item ? "Yes" : "No"));
            }
        });

        menuTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        refreshTable();
        loadCategoryFilter();
    }

    public void refreshTable() {
        try {
            List<MenuModel> list = MenuDTO.getAllMenus();
            menuTable.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    private void loadCategoryFilter() {
        try {
            categoryFilter.getItems().clear();
            categoryFilter.getItems().add("All Categories");
            categoryFilter.getItems().addAll(MenuDTO.getDistinctCategories());
            categoryFilter.setValue("All Categories");
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    public void handleSearch(KeyEvent event) {
        filterTable();
    }

    @FXML
    public void handleCategoryFilter() {
        filterTable();
    }

    private void filterTable() {
        try {
            String search = searchField.getText().trim();
            String category = categoryFilter.getValue();
            List<MenuModel> list;
            if ("All Categories".equals(category)) {
                list = search.isEmpty()
                        ? MenuDTO.getAllMenus()
                        : MenuDTO.searchMenus(search);
            } else {
                list = MenuDTO.getMenusByCategory(category);
            }
            menuTable.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    public void handleAddMenuItem() {
        openForm(null);
    }

    @FXML
    public void handleEditMenuItem() {
        MenuModel selected = menuTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a menu item");
            return;
        }
        openForm(selected);
    }

    @FXML
    public void handleDeleteMenuItem() {
        MenuModel selected = menuTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a menu item");
            return;
        }
        try {
            if (MenuDTO.deleteMenu(selected.getMenuItemId())) {
                refreshTable();
            }
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    private void openForm(MenuModel menu) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/chethana/restaurant_management_system/menu_form.fxml"
            ));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            MenuFormController controller = loader.getController();
            controller.setMenuController(this);
            if (menu != null) controller.setEditMode(menu);
            stage.showAndWait();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}