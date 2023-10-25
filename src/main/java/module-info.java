module com.example.segundoproyectodatos1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.scripting;
    requires js;


    opens com.example.segundoproyectodatos1 to javafx.fxml;
    opens servidor to java.scripting;
    exports com.example.segundoproyectodatos1;
}