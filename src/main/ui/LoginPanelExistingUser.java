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

//This class represents the login panel which is displayed if the user is an existing user.

//CITATIONS:
// The usage of the UIManager to manipulate the size of the JOptionPanes was referenced from
// https://mkyong.com/swing/java-swing-how-to-make-a-confirmation-dialog/
// The displayed icons were made using Canva.

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

    //MODIFIES: this
    //EFFECTS: Initializes the panel components, sets up their layout, and initializes JSON reader and writer.
    public LoginPanelExistingUser() {
        initComponents();
        setupLayout();
        initJson();
    }

    //EFFECTS: Initialises the JSON Writer to write to the file containing all user information, and the reader to
    //read from both the aforementioned file and the file pertaining to the current user's hub.
    private void initJson() {
        jsonWriterUserInfo = new JsonWriter(JSON_USERINFO);
        jsonReader = new JsonReader(pathForSpecificUser,JSON_USERINFO);
    }

    //EFFECTS: returns the text entered by the user in the username field.
    public String getUsername() {

        return usernameField.getText();

    }

    //MODIFIES: this
    //EFFECTS: initialises the username, password, user ID text fields, as well as the login button.
    private void initComponents() {
        JLabel textLabel = new JLabel("Welcome back!");
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        userIDField = new JTextField(20);
        loginButton = new JButton("Login");

        addActionListenerForLoginButton();

    }

    //EFFECTS: adds an action listener for the login button.
    private void addActionListenerForLoginButton() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            //EFFECTS: when the login button is clicked, the user's entered credentials are authenticated. If they match
            //the information stored on file, the login successful icon is displayed, otherwise,
            // an error message is shown.
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String userID = userIDField.getText();
                boolean loginSuccess = checkLogin(username, password, userID);
                if (loginSuccess) {
                    displayLoginSuccessfulIcon();
                    generateHubForExistingUser();
                    setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "<html><div style='text-align:center;'>Invalid Login!</div></html>", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

    //EFFECTS: Displays an icon signifying that the login was successful. the UIManager changes the size of the icon
    //as necessary to ensure the entire image is visible.
    private void displayLoginSuccessfulIcon() {
        UIManager.put("OptionPane.minimumSize", new Dimension(600, 600));
        ImageIcon imageIcon = new ImageIcon("./data/login_successful_icon.png");
        JPanel panel = new JPanel(new BorderLayout());
        JLabel imageLabel = new JLabel(imageIcon);
        panel.add(imageLabel, BorderLayout.WEST);
        JOptionPane.showMessageDialog(null, panel, "Success", JOptionPane.INFORMATION_MESSAGE);
        UIManager.put("OptionPane.minimumSize", new Dimension(400, 150));
    }

    //EFFECTS: generates an internal frame containing the hub for the existing user. Creates a new instance of the HubUI
    //class.
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

    //EFFECTS: sets up the layout of the login panel, adding the required fields as well as the respective labels.
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

    //EFFECTS: authenticates the login attempt by checking if the user's credentials matches what is on file.
    //Updates the path for specific user if the login is successful.
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

    //EFFECTS: reads the JSON file with the user information and returns the user object corresponding to
    // the provided ID. Handles the IOException accordingly.
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

    //EFFECTS: returns the value of the user field.
    public User returnUser() {
        return user;
    }

}
