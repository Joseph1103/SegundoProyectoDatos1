package Calculadora;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.util.Random;

/**
 * Esta clase representa una aplicación para generar instancias de la clase Calculadora
 * con puertos aleatorios y configurar sus parámetros de interfaz.
 */
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

    /**
     * Configura los parámetros de la interfaz de la aplicación, incluyendo un botón para
     * generar instancias de la clase Calculadora con puertos aleatorios.
     */
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

    /**
     * Genera un número aleatorio dentro de un rango específico.
     *
     * @return El número aleatorio generado.
     */
    private int generarNumRandom(){

        int min = 12000;
        int max = 50000;

        return random.nextInt(max - min + 1) + min;

    }

}

