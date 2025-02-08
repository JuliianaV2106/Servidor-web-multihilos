package org.example;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.StringTokenizer;

public class SolicitudHttp implements Runnable {
    final static String CRLF = "\r\n";

    //con este socket leo y escribo los datos con los que me comunico en el cliente
    private Socket client;

    public SolicitudHttp(Socket client){
        this.client = client;
    }
    //manejo de solicitud http del cliente
    @Override
    public void run() {
        try {
            procesarSolicitud();
        }catch(Exception e){
            System.out.println("Error al procesar solicitud" + e);
        }
    }

    private void procesarSolicitud() throws Exception {
        try(
                BufferedOutputStream out = new BufferedOutputStream(client.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))
        ){
            String lineaSolicitud;

            while((lineaSolicitud = in.readLine()) != null){
                System.out.println("Recibiendo solicitud" + lineaSolicitud);

                //se divide la soli en partes
                StringTokenizer partesL = new StringTokenizer(lineaSolicitud);
                String method = partesL.nextToken();

                if (method.equals("GET")){
                    String nombreArchivo = partesL.nextToken();
                    System.out.println("Recurso solicitado: " + nombreArchivo);

                    if (nombreArchivo.equals("/")){
                        nombreArchivo = "/index.html";
                    }

                    InputStream inputStream = getClass().getResourceAsStream(nombreArchivo);
                    long filesize = 0;
                    String lineaEstado;
                    String lineaTipo;


                    if (inputStream != null){
                        filesize = inputStream.available();
                        lineaEstado = "HTTP/1.1 200 OK" + CRLF;
                        lineaTipo = "Content-Type:" + contentType(nombreArchivo) + CRLF;
                        System.out.println("Archivo encontrado: " + nombreArchivo);
                    }
                    else{
                        lineaEstado = "HTTP/1.1 404 Not Found" + CRLF;
                        lineaTipo = "Content-Type: text/html" + CRLF;
                        System.out.println("Archivo no encontrado: " + nombreArchivo);

                        inputStream = getClass().getResourceAsStream("404.html");
                        if (inputStream == null){
                            String errorMsg = "<html><body><h1>404 Not Found</h1></body></html>";
                            inputStream = new ByteArrayInputStream(errorMsg.getBytes(StandardCharsets.UTF_8));
                            filesize = errorMsg.length();
                        }else{
                            filesize = inputStream.available();
                        }
                    }

                    enviarString(lineaEstado,out);
                    enviarString("Content-Length: "+ filesize + CRLF, out);
                    enviarString(lineaTipo,out);
                    enviarString(CRLF, out);
                    enviarBytes(inputStream, out);

                    out.flush();
                } else {
                    // Si el metodo no es GET, simplemente cerramos la conexión sin hacer nada
                    break;
                }
                while ((lineaSolicitud = in.readLine()) != null && !lineaSolicitud.isEmpty()){
                    System.out.println("Recibiendo solicitud" + lineaSolicitud);
                 }
                }
            } finally {
            client.close();
        }

    }

    private static void enviarString(String linea, OutputStream out) throws Exception {
        out.write(linea.getBytes(StandardCharsets.UTF_8));
    }

    private static void enviarBytes(InputStream inputStream, OutputStream out) throws Exception {
        byte[] buffer = new byte[1024];
        int bytes = 0;
        while ((bytes= inputStream.read(buffer)) != -1) {
            out.write(buffer, 0, bytes);
        }
    }

    private static String contentType(String nombreArchivo) {
        if (nombreArchivo.endsWith(".html") || nombreArchivo.endsWith(".htm")) {
            return "text/html";
        }
        if (nombreArchivo.endsWith(".jpg") || nombreArchivo.endsWith(".jpeg")) {
            return "image/jpg";
        }
        if (nombreArchivo.endsWith(".gif")) {
            return "image/gif";
        }
        return "application/octet-stream"; // Para cualquier otro tipo de archivo desconocido
    }
}
