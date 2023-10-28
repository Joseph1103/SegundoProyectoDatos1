package Calculadora;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.util.Random;

public class GenerarCalculadoras extends Application {

    private Pane contenedorPrincipal = new Pane();
    private Pane pane = new Pane();
    private Random random = new Random();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        //escena
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setWidth(100);
        primaryStage.setHeight(100);
        primaryStage.setResizable(false);
        primaryStage.show();
        pane.getChildren().add(contenedorPrincipal);

        parametrosInterfaz();

    }

    private void parametrosInterfaz(){

        //boton
        Button button = new Button();
        button.setOnAction(event -> {

            Calculadora calcu = new Calculadora(generarNumRandom());
            calcu.parametrosCalculadora();

        });
        button.setText("Generar Calcu");




        contenedorPrincipal.getChildren().add(button);

    }

    private int generarNumRandom(){

        int min = 12000;
        int max = 50000;

        return random.nextInt(max - min + 1) + min;

    }

}

