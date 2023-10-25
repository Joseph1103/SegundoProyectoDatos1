package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor implements Runnable{

    //Server socket
    private ServerSocket serverSocket;

    public Servidor() throws IOException {

        //Se inicia el server socket en el puerto 9999
        this.serverSocket = new ServerSocket(9999);
        System.out.println("Server noises turning ON");

        //se inicia un hilo en el propio servidor
        Thread hiloMain = new Thread(this);
        hiloMain.start();

    }
























    @Override
    public void run(){

        try {

            while (true){

                //Socket que se encarga de escuchar solicitudes entrantes de los clientes
                Socket socket = serverSocket.accept();

            }

        }catch (Exception e){
            e.printStackTrace();
        }





    }

}
