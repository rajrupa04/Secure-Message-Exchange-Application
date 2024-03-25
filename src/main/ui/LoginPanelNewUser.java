package ui;

import model.User;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginPanelNewUser extends JPanel {
    private JTextField nameOfUserField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JFrame frame;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JsonWriter jsonWriterUserInfo;
    private String pathForSpecificUser;
    private static final String JSON_USERINFO = "./data/userinfo.json";
    private User user;

    public LoginPanelNewUser(JFrame frame) {
        this.frame = frame;
        initComponents();
        setupLayout();
        initJson();
    }

    private void setupLayout() {
        setLayout(new GridLayout(4, 2));
        add(new JLabel("What should we call you?"));
        add(nameOfUserField);
        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(loginButton);
    }

    private void initJson() {
        jsonWriterUserInfo = new JsonWriter(JSON_USERINFO);
        jsonReader = new JsonReader(pathForSpecificUser,JSON_USERINFO);
    }

    private void initComponents() {
        JLabel textLabel = new JLabel("Welcome, new user!");
        nameOfUserField = new JTextField(20);
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Create new account");

        addActionListenerForLoginButton();
    }

    private void addActionListenerForLoginButton() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nameOfUser = nameOfUserField.getText();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                user = new User();
                Integer userID = loginNewUser(username, password);

                frame.setVisible(true);
                showSuccessfulLoginIcon(nameOfUser, userID);
                pathForSpecificUser = "./data/" + username + ".json";
                addNewUserToFile();
                generateHubForNewUser();
                setVisible(false);

            }
        });
    }

    private void showSuccessfulLoginIcon(String nameOfUser, Integer userID) {
        UIManager.put("OptionPane.minimumSize", new Dimension(600, 600));
        ImageIcon imageIcon = new ImageIcon("./data/login_successful_icon.png");
        Image originalImage = imageIcon.getImage();
        int desiredWidth = 500;
        int desiredHeight = 500;
        Image scaledImage = originalImage.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JPanel panel = new JPanel(new BorderLayout());
        JLabel imageLabel = new JLabel(scaledIcon);
        panel.add(imageLabel, BorderLayout.WEST);
        JLabel text = new JLabel("Account successfully created! Welcome, " + nameOfUser + ". Please note that"
                +
                " your user ID is: " + userID);
        panel.add(text,BorderLayout.SOUTH);
        JOptionPane.showMessageDialog(null, panel, "Saved", JOptionPane.INFORMATION_MESSAGE);
        UIManager.put("OptionPane.minimumSize", new Dimension(400, 150));

    }

    private void generateHubForNewUser() {
        HubUI hexisting = new HubUI(false, user);
        JInternalFrame internalFrame = new JInternalFrame("Hub UI", true, true, true,
                true);
        internalFrame.add(hexisting);
        internalFrame.setSize(400,500);
        internalFrame.pack();
        internalFrame.setVisible(true);
        JDesktopPane desktopPane = (JDesktopPane) SwingUtilities.getAncestorOfClass(JDesktopPane.class, this);
        if (desktopPane != null) {
            desktopPane.add(internalFrame);
        } else {
            JOptionPane.showMessageDialog(null, "Error: Unable to access Desktop Pane!");
        }
    }

    private void addNewUserToFile() {
        try {
            jsonWriterUserInfo.openInAppendMode();
            jsonWriterUserInfo.writeUser(user);
            jsonWriterUserInfo.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading from file!");
        }
    }

    private Integer loginNewUser(String username, String password) {
        user.createNewUser(username, password);
        Integer userID = user.getUserID();
        return userID;
    }
}
