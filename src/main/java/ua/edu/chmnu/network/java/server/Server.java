package ua.edu.chmnu.network.java.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 9555;
    private static List<ClientManager> clients = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        System.out.println("Server is running on port " + PORT + "...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                ClientManager clientManager = new ClientManager(clientSocket, clients);
                clients.add(clientManager);
                new Thread(clientManager).start();
            }
        } catch (IOException e) {
            System.err.println("Error in server: " + e.getMessage());
        }
    }
}
