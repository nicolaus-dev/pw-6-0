package ua.edu.chmnu.network.java.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientManager implements Runnable {
    private Socket clientSocket;
    private List<ClientManager> clients;
    private BufferedReader input;
    private PrintWriter output;

    public ClientManager(Socket clientSocket, List<ClientManager> clients) throws IOException {
        this.clientSocket = clientSocket;
        this.clients = clients;
        this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.output = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = input.readLine()) != null) {
                System.out.println("Message received: " + message);
                broadcastMessage(message);
            }
        } catch (IOException e) {
            System.err.println("Client disconnected: " + clientSocket.getInetAddress());
        } finally {
            cleanup();
        }
    }

    private void broadcastMessage(String message) {
        synchronized (clients) {
            for (ClientManager client : clients) {
                if (client != this) {
                    client.output.println("Message from " + clientSocket.getInetAddress() + ": " + message);
                }
            }
        }
    }

    private void cleanup() {
        try {
            input.close();
            output.close();
            clientSocket.close();
            clients.remove(this);
        } catch (IOException e) {
            System.err.println("Error cleaning up client: " + e.getMessage());
        }
    }
}
