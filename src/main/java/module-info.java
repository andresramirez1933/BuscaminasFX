module com.example.bucaminas {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.bucaminas to javafx.fxml;
    exports com.example.bucaminas;
}