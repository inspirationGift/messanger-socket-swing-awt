package main.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class ChatClient extends JFrame implements Runnable {

    private Socket socket;
    private JTextArea jTextArea;
    private JButton send, logout;
    private TextField tf;
    private DataOutputStream dout;
    private DataInputStream din;
    private Thread thread;
    private String loginName;

    public ChatClient(String loginName) throws IOException {
        super(loginName);
        this.loginName = loginName;
        this.jTextArea = new JTextArea(18, 50);
        this.tf = new TextField(50);
        this.send = new JButton("Send");
        this.logout = new JButton("Logout");
        listenersSet();
        this.socket = new Socket("localhost", 5217);
        this.din = new DataInputStream(socket.getInputStream());
        this.dout = new DataOutputStream(socket.getOutputStream());
        writeTxt(loginName, "");
        writeTxt(loginName, " LOGIN");
        this.thread = new Thread(this);
        this.thread.start();
        setup();
    }

    private void listenersSet() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                writeTxt(loginName, " LOGOUT ");
                System.exit(1);
            }
        });
        this.send.addActionListener(e -> appendMsg());
        this.logout.addActionListener(e -> {
            writeTxt(loginName, " LOGOUT ");
            System.exit(1);
        });
        this.tf.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    appendMsg();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    private void setup() {
        this.setSize(620, 420);
        this.setBackground(Color.GREEN);
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
                this.jTextArea.append("\n" + din.readUTF());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void appendMsg() {
        if (tf.getText().length() > 0) {
            writeTxt(loginName, " DATA " + tf.getText());
            tf.setText("");
        }
    }

    private void writeTxt(String loginName, String txt) {
        try {
            dout.writeUTF(loginName + txt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
