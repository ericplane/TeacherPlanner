import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class LoginInterface {
    JFrame window;
    LoginInterface(JFrame window) {
        this.window = window;
    }

    public void create(JPanel homePanel) {
        JDialog popup = new JDialog(window, "Login", Dialog.ModalityType.APPLICATION_MODAL);

        homePanel.setVisible(false);

        JPanel panel = new JPanel();
        // Add login components to the panel
        JLabel usernameLabel = new JLabel("Username: ");
        JTextField usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password: ");
        JPasswordField passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);

        popup.add(panel);

        popup.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                window.dispose();
                System.exit(0);
            }
        });

        loginButton.addActionListener(actionEvent -> registerLogin(popup, usernameField, passwordField, homePanel));

        usernameField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                    registerLogin(popup, usernameField, passwordField, homePanel);
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });

        passwordField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                    registerLogin(popup, usernameField, passwordField, homePanel);
                }
            }
        });

        popup.setSize(300, 180);
        popup.setLocationRelativeTo(window);
        popup.setResizable(false);

        popup.setVisible(true);
    }

    private void registerLogin(JDialog popup, JTextField usernameField, JPasswordField passwordField, JPanel homePanel) {
        if (usernameField.getText().equals("Alex Thompson")) {
            char[] enteredPassword = passwordField.getPassword();
            char[] correctPassword = "gamble".toCharArray();
            if (Arrays.equals(enteredPassword, correctPassword)) {
                // Password is correct
                homePanel.setVisible(true);
                popup.dispose();
            } else {
                // Incorrect password
                JOptionPane.showMessageDialog(popup, "The password you entered is incorrect.", "Password Incorrect", JOptionPane.ERROR_MESSAGE);

                // Clear the entered password
                passwordField.setText("");
            }
        } else {
            // Incorrect username
            JOptionPane.showMessageDialog(popup, "The username you entered does not exist.", "Username Not Found", JOptionPane.ERROR_MESSAGE);

            // Clear the entered username
            usernameField.setText("");
        }
    }
}
