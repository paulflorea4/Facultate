module org.example.laboratorGUI {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires kotlin.stdlib;
    requires java.sql;
    requires jbcrypt;
    requires com.fasterxml.jackson.databind;

    exports org.example.laboratorGUI.repository.database;
    exports org.example.laboratorGUI.service;

    exports org.example.laboratorGUI.domain;
    exports org.example.laboratorGUI.domain.user.duck;
    exports org.example.laboratorGUI.domain.user.person;
    exports org.example.laboratorGUI.domain.flock;
    exports org.example.laboratorGUI.domain.message;
    exports org.example.laboratorGUI.domain.friendship;

    exports org.example.laboratorGUI.utils;
    exports org.example.laboratorGUI.utils.event;
    exports org.example.laboratorGUI.events;

    exports org.example.laboratorGUI.application;

    opens org.example.laboratorGUI.application to javafx.fxml;
    exports org.example.laboratorGUI.controller;
    opens org.example.laboratorGUI.controller to javafx.fxml;
    exports org.example.laboratorGUI.utils.types;
    exports org.example.laboratorGUI.utils.GUI;
    exports org.example.laboratorGUI.domain.user;
}