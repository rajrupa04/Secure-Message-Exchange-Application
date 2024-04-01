package ui;

import model.*;
import model.Event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;

//This is the class representing the main user interface of the application. Upon running this class, the UI will
//begin to display.

//CITATIONS: the Java Swing syntax used in all the newly created UI classes has been referenced from
// https://docs.oracle.com/javase%2Ftutorial%2Fuiswing%2F%2F/components/index.html and
// https://docs.oracle.com/javase%2F7%2Fdocs%2Fapi%2F%2F/javax/swing/package-summary.html
// The usage of the UIManager to manipulate the size of the JOptionPanes was referenced from
// https://mkyong.com/swing/java-swing-how-to-make-a-confirmation-dialog/
// The displayed icons were made using Canva.

public class MainAppUI extends JPanel {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static JFrame frame;
    private JDesktopPane desktop;
    private User user;
    private EventLog eventLog;

    //MODIFIES: this
    //EFFECTS: constructs a new MainAppUI object, initialising the size, colour and state of the main frame
    // while ensuring it is visible.
    public MainAppUI() {
        frame = new JFrame("Secure Hub");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);


        eventLog = EventLog.getInstance();

        desktop = new JDesktopPane();
        desktop.setBackground(new Color(250,218,221));
        frame.add(desktop);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                printLoggedEvents();
            }
        });

        boolean isNewUser = showNewUserDialog();

        if (isNewUser) {
            loginForNewUser();
        } else {
            loginForExistingUser();
        }

        frame.pack();
        frame.setVisible(true);
    }

    //EFFECTS: Iterates through the eventlog and prints all the logged events to console when the main
    //window is closed.
    private void printLoggedEvents() {

        Iterator<Event> iterator = eventLog.iterator();
        while (iterator.hasNext()) {
            Event event = iterator.next();
            System.out.println(event.getDescription());
        }

    }

    //MODIFIES: this
    //EFFECTS: Allows an existing user to log in. The method also sets the size and state of the login panel.
    private void loginForExistingUser() {
        LoginPanelExistingUser loginPanel = new LoginPanelExistingUser();
        loginPanel.setSize(500,600);
        JInternalFrame internalFrame = createInternalFrame("Login Panel", loginPanel);
        user = loginPanel.returnUser();
        desktop.add(internalFrame);
    }

    //MODIFIES: this
    //EFFECTS: Allows a new user to set their login credentials. The method also sets the size and state of the
    // login panel.
    private void loginForNewUser() {
        LoginPanelNewUser loginPanel = new LoginPanelNewUser(frame);
        loginPanel.setSize(500,600);
        JInternalFrame internalFrame = createInternalFrame("Login Panel", loginPanel);
        desktop.add(internalFrame);
    }

    //EFFECTS: Returns an internal frame containing the specified panel with the provided title.
    private JInternalFrame createInternalFrame(String title, JPanel panel) {
        JInternalFrame internalFrame = new JInternalFrame(title, false, true, true,
                true);
        internalFrame.getContentPane().add(panel);
        internalFrame.setSize(WIDTH, HEIGHT);
        internalFrame.setVisible(true);
        return internalFrame;
    }

    //EFFECTS: Displays the dialog asking the user if they are a new user or not, returning whether the user
    //chose yes or not.
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

    //EFFECTS: Creates a new instance of the MainAppUI class, launching the application.
    public static void main(String[] args) {
        new MainAppUI();
    }

}