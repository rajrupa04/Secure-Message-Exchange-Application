package ui;

import model.User;
import persistence.JsonReader;
import persistence.JsonWriter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

//This class represents the login panel which is displayed if the user is a new user without an existing account.

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

    //MODIFIES: this
    //EFFECTS: Initializes the components of the frame, sets up layout of the components,
    // and initializes the JSON reader and writer.
    public LoginPanelNewUser(JFrame frame) {
        this.frame = frame;
        initComponents();
        setupLayout();
        initJson();
    }

    //EFFECTS: Adds the required components along with their respective labels to the login panel.
    private void setupLayout() {
        setLayout(new GridLayout(4, 2));
        add(new JLabel("What should we call you?"));
        add(nameOfUserField);
        add(new JLabel("Set username:"));
        add(usernameField);
        add(new JLabel("Set password:"));
        add(passwordField);
        add(loginButton);
    }

    //EFFECTS: Initialises the JSON Writer to write to the file containing all user information, and the reader to
    //read from both the aforementioned file and the file pertaining to the current user's hub.
    private void initJson() {
        jsonWriterUserInfo = new JsonWriter(JSON_USERINFO);
        jsonReader = new JsonReader(pathForSpecificUser,JSON_USERINFO);
    }

    //EFFECTS: Initializes the username field, password field, and login button.
    private void initComponents() {
        JLabel textLabel = new JLabel("Welcome, new user!");
        nameOfUserField = new JTextField(20);
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Create new account");

        addActionListenerForLoginButton();
    }

    //EFFECTS: Adds an action listener to the login button.
    private void addActionListenerForLoginButton() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            //EFFECTS: When the login button is clicked, the user's entries are retrieved and saved to the system.
            //The path for the specific user is updated, and the user's generated user ID is displayed. The login panel
            //is then hidden.
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

    //EFFECTS: Displays an icon signifying that the login was successful. the UIManager changes the size of the icon
    //as necessary to ensure the entire image is visible.
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

    //EFFECTS: Generates and displays a blank hub for a new user.
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

    //EFFECTS: Writes the new user to the JSON file containing the user information.
    private void addNewUserToFile() {
        try {
            jsonWriterUserInfo.openInAppendMode();
            jsonWriterUserInfo.writeUser(user);
            jsonWriterUserInfo.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading from file!");
        }
    }

    //EFFECTS: creates a new user in the system with the provided username and password, returning generated user ID.
    private Integer loginNewUser(String username, String password) {
        user.createNewUser(username, password);
        Integer userID = user.getUserID();
        return userID;
    }
}
