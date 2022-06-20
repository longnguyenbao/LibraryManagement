module com.nbl.librarymanagementapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires java.base;
    requires java.sql;

    opens com.nbl.librarymanagementapp to javafx.fxml;
    exports com.nbl.librarymanagementapp;
    exports com.nbl.pojo;
}