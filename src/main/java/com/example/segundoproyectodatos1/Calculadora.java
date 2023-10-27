package com.example.segundoproyectodatos1;

import auxiliares.InfixToPostfix;
import auxiliares.Mensaje;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;

public class Calculadora extends Application {

    //contenedor
    Pane pane = new Pane();
    Pane contenedorPrincipal = new Pane();

    //instancia de convertidor de expresion a polaco
    InfixToPostfix infixToPostfix = new InfixToPostfix();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        //escena
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setWidth(600);
        primaryStage.setHeight(411);
        primaryStage.setResizable(false);
        primaryStage.show();
        pane.getChildren().add(contenedorPrincipal);

        //carga los elementos graficos de la interfaz
        parametrosCalculadora();

    }

    public void parametrosCalculadora(){

        //imagen de la interfaz
        Image image = new Image(getClass().getResourceAsStream("background.jpg"));

        //image view para mostrar la imagen
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(600);
        imageView.setFitHeight(411);

        //caja de texto
        TextField textField = new TextField();
        textField.setMinWidth(340);
        textField.setMinHeight(30);
        textField.setTranslateX(70);
        textField.setTranslateY(170);

        //boton para enviar la info al server
        Button button = new Button();
        button.setText("Calcular");
        button.setStyle("-fx-font-size: 14");
        button.setTranslateX(205);
        button.setTranslateY(250);
        button.setOnAction(e -> convertirExpresionAPolaco(textField.getText()));

        //agregar elementos a la interfaz
        contenedorPrincipal.getChildren().addAll(
                imageView,
                textField,
                button
        );

    }

    private void convertirExpresionAPolaco(String expresion){

        //convertir la expresion a polaco
        String exprePolaco = infixToPostfix.conversionPosfijo(expresion);

        enviarMensajeServidor("calcular",expresion);

    }

    private void enviarMensajeServidor(String accion){

        try {
            //se crea el puente
            Socket socket = new Socket("localhost", 9999);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            //se crea el objeto mensaje para enviar la informacion
            Mensaje mensaje = new Mensaje();
            mensaje.setAccion(accion);

            out.writeObject(mensaje);
            out.flush();

            socket.close();
            out.close();


        } catch (ConnectException e){
            System.out.println("Conexion Rechazada");
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    private void enviarMensajeServidor(String accion, String expresion){

        try {
            //se crea el puente
            Socket socket = new Socket("localhost", 9999);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            //se crea el objeto mensaje para enviar la informacion
            Mensaje mensaje = new Mensaje();
            mensaje.setAccion(accion);
            mensaje.setExpresionMatematica(expresion);

            out.writeObject(mensaje);
            out.flush();

            socket.close();
            out.close();


        } catch (ConnectException e){
            System.out.println("Conexion Rechazada");
        } catch (IOException e){
            e.printStackTrace();
        }

    }








}
