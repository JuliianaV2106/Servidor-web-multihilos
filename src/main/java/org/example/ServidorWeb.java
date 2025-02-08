package org.example;

import java.io.*;
import java.net.*;

public class ServidorWeb {
    private ServerSocket server;

    public ServidorWeb(int puerto) throws IOException {

        server = new ServerSocket(puerto);
        System.out.println("El servidor se conecto en el puerto " + puerto );
    }

    public void iniciar() throws IOException {
        while (true) {

            Socket client = server.accept();
            System.out.println("Conexión aceptada desde " + client.getInetAddress());

            Thread hiloSolicitud = new Thread(new SolicitudHttp(client));
            hiloSolicitud.start();
        }
    }

}
