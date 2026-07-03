module org.example.template {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires javafx.base;
    requires javafx.graphics;


    opens org.example.template to javafx.fxml;
    exports org.example.template;
    exports org.example.template.domain;
    exports org.example.template.observer.events;
}