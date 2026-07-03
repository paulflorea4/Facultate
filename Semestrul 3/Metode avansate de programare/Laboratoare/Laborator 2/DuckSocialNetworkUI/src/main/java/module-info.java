module org.example.ducksocialnetworkui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.base;
    requires java.desktop;
    requires jbcrypt;

    opens org.example.ducksocialnetworkui to javafx.fxml;
    opens org.example.ducksocialnetworkui.application to javafx.graphics, javafx.fxml;
    opens org.example.ducksocialnetworkui.controller to javafx.fxml;
    opens org.example.ducksocialnetworkui.domain to javafx.base;
    opens org.example.ducksocialnetworkui.event to javafx.base;

    exports org.example.ducksocialnetworkui;
    exports org.example.ducksocialnetworkui.application;
}
