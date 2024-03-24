package ui;

import model.*;

import javax.swing.*;
import java.awt.*;

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

        return input == JOptionPane.YES_OPTION;
    }

    public static void main(String[] args) {
        new MainAppUI();
    }
}