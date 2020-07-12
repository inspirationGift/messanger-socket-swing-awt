package main.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;


public class ChatServer {
    protected static Vector<Socket> ClientSockets;
    protected static Vector<String> LoginNames;

    ChatServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(5217);
        ClientSockets = new Vector<>();
        LoginNames = new Vector<>();

        while (true) {
            Socket client = serverSocket.accept();
            new AcceptClient(client);
        }
    }


}


