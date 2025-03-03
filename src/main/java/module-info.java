module com.example.librarymanage {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;

    opens com.example.librarymanage to javafx.fxml;
    exports com.example.librarymanage;
}