module com.example.bucaminas {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.poli.bucaminas to javafx.fxml;
    exports com.poli.bucaminas;
}