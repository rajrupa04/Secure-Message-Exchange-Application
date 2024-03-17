package ui;

import model.User;
import org.json.JSONObject;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginPanelExistingUser extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField userIDField;
    private JButton loginButton;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JsonWriter jsonWriterUserInfo;
    private String pathForSpecificUser;
    private static final String JSON_USERINFO = "./data/userinfo.json";
    private User user;

    public LoginPanelExistingUser() {
        initComponents();
        setupLayout();
        initJson();
    }

    private void initJson() {
        jsonWriterUserInfo = new JsonWriter(JSON_USERINFO);
        jsonReader = new JsonReader(pathForSpecificUser,JSON_USERINFO);
    }

    public String getUsername() {

        return usernameField.getText();

    }

    private void initComponents() {
        JLabel textLabel = new JLabel("Welcome back!");
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        userIDField = new JTextField(20);
        loginButton = new JButton("Login");

        addActionListenerForLoginButton();

    }

    private void addActionListenerForLoginButton() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String userID = userIDField.getText();
                boolean loginSuccess = checkLogin(username, password, userID);
                if (loginSuccess) {
                    JOptionPane.showMessageDialog(null, "Login Successful!");
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Login!");
                }
                generateHubForExistingUser();
                setVisible(false);

            }
        });

    }

    private void generateHubForExistingUser() {
        HubUI hexisting = new HubUI(true, user);
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

    private void setupLayout() {
        setLayout(new GridLayout(4, 2));
        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(new JLabel("User ID:"));
        add(userIDField);
        add(loginButton);
    }

    private boolean checkLogin(String username, String password, String userID) {
        Integer id = Integer.parseInt(userID);
        User userFromFile = readUserFromFile(id);
        this.user = userFromFile;
        Boolean successfulLogin = userFromFile.userLogIn(id, username, password);
        if (successfulLogin) {
            pathForSpecificUser = "./data/" + username + ".json";
            return true;
        } else {
            return false;

        }
    }

    public User readUserFromFile(Integer id) {
        try {
            JSONObject userJsonObject = jsonReader.returnJsonObject(JSON_USERINFO);
            User u = jsonReader.getUserByID(id, userJsonObject);
            return u;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading from file!");
        }
        return null;
    }

    public User returnUser() {
        return user;
    }

}
