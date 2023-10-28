package Calculadora;

import auxiliares.InfixToPostfix;
import auxiliares.Mensaje;
import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;


/**
 * Esta clase representa una calculadora que permite a los usuarios ingresar expresiones
 * matemáticas en notación infija, convertirlas a notación polaca inversa y calcular el
 * resultado utilizando un servidor remoto.
 */
public class Calculadora implements Runnable{

    //contenedor
    private Pane pane = new Pane();
    private Pane contenedorPrincipal = new Pane();
    private Label respuesta = new Label();

    private ServerSocket serverSocket;

    private int puerto;

    //instancia de convertidor de expresion a polaco
    InfixToPostfix infixToPostfix = new InfixToPostfix();

    /**
     * Crea una instancia de la calculadora y establece el puerto en el que escuchará
     * las conexiones entrantes.
     *
     * @param puerto El puerto en el que se escucharán las conexiones entrantes.
     */
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

    /**
     * Configura los parámetros de la interfaz de la calculadora, incluyendo la escena,
     * campos de entrada y botones.
     */
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

    /**
     * Convierte una expresión en notación infija a notación polaca inversa y la envía
     * al servidor para su cálculo.
     *
     * @param expresion La expresión en notación infija a convertir y calcular.
     */
    private void convertirExpresionAPolaco(String expresion){

        enviarMensajeServidor("calcular",expresion);

    }
    /**
     * Procesa un mensaje recibido del servidor, realizando acciones correspondientes
     * según la acción especificada en el mensaje.
     *
     * @param mensaje El mensaje recibido del servidor.
     */
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

            case "historial" -> {



            }

        }

    }





    /**
     * Envía un mensaje al servidor con la acción especificada.
     *
     * @param accion La acción a enviar al servidor.
     */
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

    /**
     * Envía un mensaje al servidor con la acción y la expresión matemática especificadas.
     *
     * @param accion     La acción a enviar al servidor.
     * @param expresion  La expresión matemática a enviar al servidor.
     */

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

    /**
     * Este método representa la lógica de un hilo que escucha las solicitudes entrantes
     * de los clientes en el servidor. Cuando se recibe una solicitud, se procesa el mensaje
     * del cliente llamando al método 'procesarMensajeServidor'.
     */
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
