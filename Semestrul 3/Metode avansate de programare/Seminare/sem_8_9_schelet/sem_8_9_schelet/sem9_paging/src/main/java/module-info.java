module scs.map.sem9_paging {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires javafx.graphics;
    requires org.postgresql.jdbc;

    opens scs.map.sem9_paging to javafx.fxml;
    exports scs.map.sem9_paging;
    exports scs.map.sem9_paging.config;
    opens scs.map.sem9_paging.config to javafx.fxml;
}