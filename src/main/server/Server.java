package main.server;

import java.io.IOException;

public class Server {
    public static void main(String[] args) {
        try {
            new ChatServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
