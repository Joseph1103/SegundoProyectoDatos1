module com.example.segundoproyectodatos {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.scripting;
    requires js;

    opens servidor to java.scripting;
    opens Calculadora to javafx.fxml;

    exports Calculadora;
}