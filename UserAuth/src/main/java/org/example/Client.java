package org.example;

import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // Esto se cambia por lo que pone el usuario en la pantalla de login
            String username = "user1";
            String password = "pass1";

            out.println(username);
            out.println(password);

            String response = in.readLine();
            // En vez de este print revisa si el servidor confirmó el usuario y sigue con la aplicación
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
