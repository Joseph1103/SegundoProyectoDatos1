package servidor;

import auxiliares.InfixToPostfix;
import auxiliares.Mensaje;
import auxiliares.RPNConverter;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.io.*;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Esta clase representa un servidor que escucha solicitudes entrantes de clientes
 * y realiza cálculos matemáticos utilizando expresiones matemáticas en notación infija.
 */
public class Servidor implements Runnable{

    //Server socket
    private ServerSocket serverSocket;

    //convertidor de expresion matematica a polaca
    InfixToPostfix infixToPostfix = new InfixToPostfix();

    //convertir polaco a arbol de expresion
    RPNConverter converter = new RPNConverter();

    /**
     * Crea una instancia del servidor y comienza a escuchar en el puerto 9999.
     *
     * @throws IOException Si ocurre un error al iniciar el servidor.
     */
    public Servidor() throws IOException {

        //Se inicia el server socket en el puerto 9999
        this.serverSocket = new ServerSocket(9999);
        System.out.println("Server noises turning ON");

        //se inicia un hilo en el propio servidor
        Thread hiloMain = new Thread(this);
        hiloMain.start();

        //realizarOperacionMatematica("(2+3)*((2+3)+1)");

    }
    /**
     * Realiza una operación matemática dada una expresión matemática en notación infija.
     *
     * @param expresionMatematica La expresión matemática en notación infija.
     * @return El resultado de la operación.
     */
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
    /**
     * Elimina los espacios en blanco al final de una cadena de texto.
     *
     * @param input La cadena de texto que se va a procesar.
     * @return La cadena de texto sin espacios en blanco al final.
     */
    private String  eliminarEspacioFinal(String input){
        return input.replaceAll("\\s+$", "");
    }
    /**
     * Procesa un mensaje recibido del cliente y toma acciones correspondientes
     * según la acción especificada en el mensaje.
     *
     * @param mensaje El mensaje recibido del cliente.
     */
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

    /**
     * Envía un mensaje de respuesta al cliente.
     *
     * @param respuesta       La respuesta que se enviará al cliente.
     * @param accion          La acción que se especificará en el mensaje.
     * @param mensajeCliente El mensaje original del cliente para obtener el puerto de destino.
     */
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
    /**
     * Método principal para iniciar el servidor.
     *
     * @param args Argumentos de la línea de comandos (no se utilizan).
     * @throws IOException Si ocurre un error al iniciar el servidor.
     */
    //iniciar el servidor
    public static void main(String [] args) throws IOException {
        Servidor servidor = new Servidor();
    }

    /**
     * Este método representa la lógica de un hilo que escucha las solicitudes entrantes
     * de los clientes en el servidor. Cuando se recibe una solicitud, se procesa el mensaje
     * del cliente llamando al método 'procesarMensajeCliente'.
     */
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
