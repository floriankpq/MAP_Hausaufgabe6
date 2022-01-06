module com.example.labor_6 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.junit.jupiter.api;
    requires org.mockito;


    opens com.example.labor_6 to javafx.fxml;
    exports com.example.labor_6;
}