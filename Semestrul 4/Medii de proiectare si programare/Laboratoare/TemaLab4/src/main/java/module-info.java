module org.example.lab {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires spring.context;
    requires spring.beans;
    requires spring.core;

    requires org.apache.logging.log4j;
    requires org.apache.commons.logging;

    opens org.example.lab4.ctrl to javafx.fxml;
    opens org.example.lab4.model to javafx.base, javafx.fxml, spring.beans, spring.context, spring.core; // add this

    exports org.example.lab4.repository.file to spring.beans, spring.context, spring.core;
    exports org.example.lab4.repository.jdbc to spring.beans, spring.context, spring.core;
    exports org.example.lab4.services to spring.beans, spring.context, spring.core;
    exports org.example.lab4.model to spring.beans, spring.context, spring.core;
    exports org.example.lab4.repository to spring.beans, spring.context, spring.core;

    opens org.example.lab4.repository.file to spring.beans, spring.context, spring.core;
    opens org.example.lab4.repository.jdbc to spring.beans, spring.context, spring.core;
    opens org.example.lab4.services to spring.beans, spring.context, spring.core;
    opens org.example.lab4 to spring.beans, spring.context, spring.core;

    exports org.example.lab4;
}