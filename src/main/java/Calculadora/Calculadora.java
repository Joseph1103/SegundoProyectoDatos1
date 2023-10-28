package Calculadora;

import auxiliares.InfixToPostfix;
import auxiliares.Mensaje;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;

public class Calculadora implements Runnable{

    //contenedor
    private Pane pane = new Pane();
    private Pane contenedorPrincipal = new Pane();
    private Label respuesta = new Label();

    private ServerSocket serverSocket;

    private int puerto;

    //instancia de convertidor de expresion a polaco
    InfixToPostfix infixToPostfix = new InfixToPostfix();

    public Calculadora(int puerto){

        try {

            //asignar el puerto de la instancia
            this.puerto = puerto;

            //puerto a la escucha de conexiones
            this.serverSocket = new ServerSocket(puerto);

            //iniciar el hilo local
            Thread thread = new Thread(this);
            thread.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void parametrosCalculadora(){

        //escena
        Scene scene = new Scene(pane);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.setWidth(600);
        primaryStage.setHeight(411);
        primaryStage.setResizable(false);
        primaryStage.show();
        pane.getChildren().add(contenedorPrincipal);

        //imagen de la interfaz
        Image image = new Image(getClass().getResourceAsStream("/background.jpg"));

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

        //espacio para desplegar la respuesta
        respuesta.setTextFill(Color.WHITE);
        respuesta.setText("");
        respuesta.setTranslateX(425);
        respuesta.setTranslateY(178);

        //agregar elementos a la interfaz
        contenedorPrincipal.getChildren().addAll(
                imageView,
                textField,
                button,
                respuesta
        );

    }

    private void convertirExpresionAPolaco(String expresion){

        //convertir la expresion a polaco
        String exprePolaco = infixToPostfix.conversionPosfijo(expresion);

        enviarMensajeServidor("calcular",expresion);

    }

    private void procesarMensajeServidor(Mensaje mensaje){

        switch (mensaje.getAccion()){

            case "respuesta_calculo" -> {

                Object respuestaExpresion = mensaje.getRespuesta();

                if (respuestaExpresion instanceof Integer){
                    Platform.runLater(()->{
                        int aux = (Integer) respuestaExpresion;
                        respuesta.setText(String.valueOf(aux));

                    });
                }else if (respuestaExpresion instanceof Long){
                    Platform.runLater(()->{
                        long aux = (Long) respuestaExpresion;
                        respuesta.setText(String.valueOf(aux));

                    });
                }else if (respuestaExpresion instanceof Double){
                    Platform.runLater(()->{
                        double aux = (Double) respuestaExpresion;
                        respuesta.setText(String.valueOf(aux));

                    });
                }

            }

        }

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
            mensaje.setPuerto(puerto);

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

    @Override
    public void run(){

        try {

            while (true){

                //Socket que se encarga de escuchar solicitudes entrantes de los clientes
                Socket socket = serverSocket.accept();

                System.out.println("Cliente conectado desde " + socket.getInetAddress().getHostAddress());

                //leer la cadena de texto del socket
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                Mensaje mensajeServer = (Mensaje) in.readObject();

                //manda a procesar el mensaje del cliente
                procesarMensajeServidor(mensajeServer);

            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }





}
