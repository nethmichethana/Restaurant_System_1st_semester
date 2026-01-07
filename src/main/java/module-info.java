module com.chethana.restaurant_management_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires javafx.graphics;
    requires java.sql;
    requires java.base;
    requires net.sf.jasperreports.core;
    //requires com.chethana.restaurant_management_system;

    opens com.chethana.restaurant_management_system to javafx.fxml;
    exports com.chethana.restaurant_management_system;
    opens com.chethana.restaurant_management_system.controllers to javafx.fxml;
    exports com.chethana.restaurant_management_system.controllers;
    exports com.chethana.restaurant_management_system.model;
    exports com.chethana.restaurant_management_system.dto;
}