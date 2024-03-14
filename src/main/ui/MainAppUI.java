package ui;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainAppUI {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static JFrame frame;
    private JDesktopPane desktop;

    public MainAppUI() {
        frame = new JFrame("Secure Hub Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);


        desktop = new JDesktopPane();
        desktop.addMouseListener(new DesktopFocusAction());

        boolean isNewUser = showNewUserDialog();

        if (isNewUser) {
            JOptionPane.showMessageDialog(frame, "Welcome, new user!");
            //TO ADD
        } else {

            LoginPanelExistingUser loginPanel = new LoginPanelExistingUser(frame);
            JInternalFrame internalFrame = new JInternalFrame("Login Panel", false, true,
                    true, true);
            frame.getContentPane().add(loginPanel);
            internalFrame.add(loginPanel);
            internalFrame.setSize(WIDTH,HEIGHT);
            internalFrame.setVisible(true);
            desktop.add(internalFrame);


            frame.getContentPane().add(desktop);


            frame.pack();
            frame.setVisible(true);
        }

        frame.pack();
        frame.setVisible(true);
    }



    private boolean showNewUserDialog() {
        int option = JOptionPane.showConfirmDialog(frame, "Welcome! Are you a new user?",
                "Secure Hub Login", JOptionPane.YES_NO_OPTION);
        return option == JOptionPane.YES_OPTION;
    }

    private class DesktopFocusAction extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            MainAppUI.frame.requestFocusInWindow();
        }
    }

    public static void main(String[] args) {
        new MainAppUI();
    }
}
