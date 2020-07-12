package main.client;


import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class LoginWindow {

    private JFrame frame;
    private JPanel panel;
    private JTextField userName;
    private JButton button;
    private ChatClient chatClient;

    public LoginWindow() {
        this.frame = new JFrame("Login");
        this.panel = new JPanel();
        this.userName = new JTextField(20);
        this.button = new JButton("Login");

        buildFrame();
        clickButtonListener();
        keyListenerForLogin();
    }

    public ChatClient getChatClient() {
        try {
            this.chatClient = new ChatClient(userName.getText());
            frameOnClose();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.chatClient;
    }

    private void buildFrame() {
        panel.add(userName);
        panel.add(button);
        frame.setSize(300, 100);
        frame.add(panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private void clickButtonListener() {
        this.button.addActionListener(e -> getChatClient());
    }

    private void frameOnClose() {
        this.frame.setVisible(false);
        this.frame.dispose();
    }

    private void keyListenerForLogin() {
        userName.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    getChatClient();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }


}
