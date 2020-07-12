package main.client;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class ChatClient extends JFrame implements Runnable {

    Socket socket;
    JTextArea jTextArea;
    JButton send, logout;
    TextField tf;
    DataOutputStream dout;
    DataInputStream din;
    Thread thread;

    String loginName;

    public ChatClient(String loginName) throws IOException {
        super(loginName);
        this.loginName = loginName;
        this.jTextArea = new JTextArea(18, 50);
        this.tf = new TextField(50);

        this.send = new JButton("Send");
        this.logout = new JButton("Logout");
        this.send.addActionListener(e -> {
            try {
                dout.writeUTF(loginName + " DATA " + tf.getText());
                tf.setText("");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        this.logout.addActionListener(e -> {
            try {
                dout.writeUTF(loginName + " LOGOUT ");
                System.exit(1);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        this.socket = new Socket("localhost", 5217);
        this.din = new DataInputStream(socket.getInputStream());
        this.dout = new DataOutputStream(socket.getOutputStream());
        this.dout.writeUTF(loginName);
        this.dout.writeUTF(loginName + " " + "LOGIN");
        this.thread = new Thread(this);
        this.thread.start();
        setup();

    }

    private void setup() {
        this.setSize(600, 400);
        JPanel jPanel = new JPanel();
        jPanel.add(new JScrollPane(jTextArea));
        jPanel.add(tf);
        jPanel.add(send);
        jPanel.add(logout);
        this.add(jPanel);
        this.setVisible(true);
    }

    @Override
    public void run() {
        while (true) {
            try {
                jTextArea.append("\n" + din.readUTF());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
