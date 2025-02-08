package org.example;
import java.io.IOException;


    public class Main {
        public static void main(String[] args) {
            try {
                ServidorWeb server = new ServidorWeb(8080);
                server.iniciar();
            } catch (IOException e) {
                System.out.println("Error al iniciar el servidor: " + e.getMessage());
            }
        }
    }
