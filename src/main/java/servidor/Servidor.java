package servidor;

import auxiliares.InfixToPostfix;
import auxiliares.Mensaje;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor implements Runnable{

    //Server socket
    private ServerSocket serverSocket;

    //arbol de expresion
    //ArbolExpresion arbolExpresion = new ArbolExpresion();

    NPItoInfix npItoInfix = new NPItoInfix();

    //convertidor de expresion matematica a polaca
    InfixToPostfix infixToPostfix = new InfixToPostfix();

    public Servidor() throws IOException {

        //Se inicia el server socket en el puerto 9999
        this.serverSocket = new ServerSocket(9999);
        System.out.println("Server noises turning ON");

        //se inicia un hilo en el propio servidor
        Thread hiloMain = new Thread(this);
        hiloMain.start();

        //realizarOperacionMatematica("(2+3)*((2+3)+1)");

    }

    private Object realizarOperacionMatematica(String expresionMatematica){

        //convertir expresion matemática a postorder
        String expreToPolaco = infixToPostfix.conversionPosfijo(expresionMatematica);

        //transformar la expresion polaca a una que el arbol pueda entender
        System.out.println("expresion polaca: " + expreToPolaco);

        //hacer el calculo de la expresion
        //return arbolExpresion.realizarOperacion(expreToPolaco);

        npItoInfix.realizarOperacion(expreToPolaco);

        return "hi";

    }

    private void procesarMensajeCliente(Mensaje mensaje){

        switch (mensaje.getAccion()){

            case "calcular" -> {

                Object respuesta = realizarOperacionMatematica(mensaje.getExpresionMatematica());

                if (respuesta instanceof Integer){

                    respuesta = (int) respuesta;

                }
                else if (respuesta instanceof Float) {

                    respuesta = (float) respuesta;

                }

                System.out.println(respuesta);


            }

            case "registro" -> {



            }

        }

    }

    //iniciar el servidor
    public static void main(String [] args) throws IOException {
        Servidor servidor = new Servidor();
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

                Mensaje mensajeCliente = (Mensaje) in.readObject();

                //manda a procesar el mensaje del cliente
                procesarMensajeCliente(mensajeCliente);

            }

        }catch (Exception e){
            e.printStackTrace();
        }





    }

}
