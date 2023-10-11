package com.example.segundoproyectodatos1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Cargar el archivo FXML que define la interfaz de usuario
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Scene1.fxml"));
        // Crear una escena a partir del contenido del archivo FXML cargado
        Scene scene = new Scene(fxmlLoader.load());
        // Configurar el título de la ventana
        stage.setTitle("Calculator");
        // Establecer la escena en el escenario principal
        stage.setScene(scene);
        // Mostrar la ventana
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}