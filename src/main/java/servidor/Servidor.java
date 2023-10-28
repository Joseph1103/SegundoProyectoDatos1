package servidor;

import auxiliares.InfixToPostfix;
import auxiliares.Mensaje;
import auxiliares.RPNConverter;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.io.*;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor implements Runnable{

    //Server socket
    private ServerSocket serverSocket;

    //convertidor de expresion matematica a polaca
    InfixToPostfix infixToPostfix = new InfixToPostfix();

    //convertir polaco a arbol de expresion
    RPNConverter converter = new RPNConverter();

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

        //borra el espacio generado al final de la expresion para evitar errores en la conversion a postfix
        expreToPolaco = eliminarEspacioFinal(expreToPolaco);

        //convierte la expresion polaca a un arbol de expresion
        String resultExpresion = converter.convertirInfixToPostFix(expreToPolaco);

        //convertir la expresion matemática del string a un resultado
        Context rhino = Context.enter();
        Scriptable scope = rhino.initStandardObjects();

        try{
            //hace la operación aritmética y la almacena
            Object result = rhino.evaluateString(scope, resultExpresion,"Evaluacion",1,null);

            return result;

        }finally {
            Context.exit();
        }



    }

    private String  eliminarEspacioFinal(String input){
        return input.replaceAll("\\s+$", "");
    }

    private void procesarMensajeCliente(Mensaje mensaje){

        switch (mensaje.getAccion()){

            case "calcular" -> {

                Object respuesta = realizarOperacionMatematica(mensaje.getExpresionMatematica());

                System.out.println(respuesta);

                enviarMensajeCliente(respuesta,"respuesta_calculo",mensaje);

            }

            case "registro" -> {



            }

        }

    }

    //private void

    private void enviarMensajeCliente(Object respuesta, String accion,Mensaje mensajeCliente){

        try {
            //informacion del resultado
            Mensaje mensaje = new Mensaje();
            mensaje.setAccion(accion);
            mensaje.setRespuesta(respuesta);

            //crear el socket para enviar el mensaje
            Socket socket = new Socket("localhost", mensajeCliente.getPuerto());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

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

                System.out.println("Cliente conectado desde " + socket.getInetAddress().getHostAddress() + ", Puerto: " + socket.getPort());

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
