module com.example.segundoproyectodatos1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.segundoproyectodatos1 to javafx.fxml;
    exports com.example.segundoproyectodatos1;
}