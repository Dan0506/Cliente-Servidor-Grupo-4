package org.example;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is listening on port 12345");

            while (true) {
                Socket socket = serverSocket.accept();
                new AuthThread(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class AuthThread extends Thread {
    private Socket socket;

    private static final List<List<String>> USERS = Arrays.asList(
            //Usuarios de prueba, guarde aqui los usuarios que guarda el programa cuando se registra un nuevo usuario
            Arrays.asList("user1", "pass1"),
            Arrays.asList("user2", "pass2"),
            Arrays.asList("user3", "pass3")
    );

    public AuthThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {

            String username = in.readLine();
            String password = in.readLine();

            boolean authenticated = USERS.stream()
                    .anyMatch(user -> user.get(0).equals(username) && user.get(1).equals(password));

            if (authenticated) {
                //Para el cliente, por ejemplo puede poner un if que revise si la respuesta fue "Usuario autenticado" y ya con eso se mete a la app
                out.println("Usuario autenticado");
            } else {
                out.println("Usuario o contraseña incorrecto");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
