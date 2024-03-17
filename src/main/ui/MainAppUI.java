package ui;

import model.*;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainAppUI extends JPanel {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static JFrame frame;
    private JDesktopPane desktop;
    private User user;

    public MainAppUI() {
        frame = new JFrame("Secure Hub Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);

        desktop = new JDesktopPane();
        frame.add(desktop);

        boolean isNewUser = showNewUserDialog();

        if (isNewUser) {
            loginForNewUser();
        } else {
            loginForExistingUser();
        }

        frame.pack();
        frame.setVisible(true);
    }

    private void loginForExistingUser() {
        LoginPanelExistingUser loginPanel = new LoginPanelExistingUser();
        JInternalFrame internalFrame = createInternalFrame("Login Panel", loginPanel);
        user = loginPanel.returnUser();
        desktop.add(internalFrame);
    }

    private void loginForNewUser() {
        LoginPanelNewUser loginPanel = new LoginPanelNewUser(frame);
        JInternalFrame internalFrame = createInternalFrame("Login Panel", loginPanel);
        desktop.add(internalFrame);
    }

    private JInternalFrame createInternalFrame(String title, JPanel panel) {
        JInternalFrame internalFrame = new JInternalFrame(title, false, true, true,
                true);
        internalFrame.getContentPane().add(panel);
        internalFrame.setSize(WIDTH, HEIGHT);
        internalFrame.setVisible(true);
        return internalFrame;
    }

    private boolean showNewUserDialog() {
        int option = JOptionPane.showConfirmDialog(frame, "Welcome! Are you a new user?",
                "Secure Hub Login", JOptionPane.YES_NO_OPTION);
        return option == JOptionPane.YES_OPTION;
    }

    public static void main(String[] args) {
        new MainAppUI();
    }
}