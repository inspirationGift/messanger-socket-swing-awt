package main.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

import static main.server.ChatServer.ClientSockets;
import static main.server.ChatServer.LoginNames;


public class AcceptClient extends Thread {

    private DataInputStream din;
    private String msg;
    private String loginName;

    public AcceptClient(Socket client) throws IOException {
        this.din = new DataInputStream(client.getInputStream());
        this.loginName = din.readUTF();
        LoginNames.add(loginName);
        ClientSockets.add(client);
        this.msg = "";
        this.start();
    }

    @Override
    public void run() {

        while (true) {

            String msgFromClient = null;
            try {
                msgFromClient = din.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert msgFromClient != null;
            StringTokenizer st = new StringTokenizer(msgFromClient);
            String loginName = st.nextToken();
            String msgType = st.nextToken();

            while (st.hasMoreTokens()) {
                this.msg = " " + st.nextToken();
            }

            if (msgType.equals("LOGIN")) {
                getSocketMessage(loginName, "  has logged in", false);
            } else if (msgType.equals("LOGOUT")) {
                getSocketMessage(loginName, " has logged out", true);
                break; // to stop loop
            } else {
                getSocketMessage(loginName, ": " + msg, false);
            }
        }
    }

    private void getSocketMessage(String loginName, String msg, boolean cleanSocket) {
        for (int i = 0; i < LoginNames.size(); i++) {
            Socket socket = ClientSockets.elementAt(i);
            if (cleanSocket && loginName.equals(LoginNames.elementAt(i))) {
                LoginNames.remove(i);
                ClientSockets.remove(i);
            }
            DataOutputStream dOut;
            try {
                dOut = new DataOutputStream(socket.getOutputStream());
                dOut.writeUTF(loginName + msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}