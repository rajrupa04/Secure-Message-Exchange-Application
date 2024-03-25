package ui;

import model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainAppUI extends JPanel {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static JFrame frame;
    private JDesktopPane desktop;
    private User user;

    public MainAppUI() {
        frame = new JFrame("Secure Hub");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);



        desktop = new JDesktopPane();
        desktop.setBackground(new Color(250,218,221));
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
        loginPanel.setSize(500,600);
        JInternalFrame internalFrame = createInternalFrame("Login Panel", loginPanel);
        user = loginPanel.returnUser();
        desktop.add(internalFrame);
    }

    private void loginForNewUser() {
        LoginPanelNewUser loginPanel = new LoginPanelNewUser(frame);
        loginPanel.setSize(500,600);
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

        ImageIcon icon = new ImageIcon("./data/secure_hub_login_logo.png");


        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 182, 193));
        panel.setSize(new Dimension(200, 64));
        panel.setLayout(null);

        JLabel label1 = new JLabel("Welcome! Are you a new user?");
        label1.setVerticalAlignment(SwingConstants.BOTTOM);
        label1.setFont(new Font("Arial", Font.PLAIN, 12));
        label1.setBounds(0, 0, 200, 32);
        label1.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label1);

        UIManager.put("OptionPane.minimumSize", new Dimension(1000, 600));
        int input = JOptionPane.showConfirmDialog(null, panel, "Secure Hub Login",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, icon);

        UIManager.put("OptionPane.minimumSize", new Dimension(400, 150));
        return input == JOptionPane.YES_OPTION;
    }

    public static void main(String[] args) {
        new MainAppUI();
    }
}