package ui;

import model.Hub;
import model.User;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class HubUI extends JPanel {
    private JTabbedPane hubTabs;
    private NotesUI notes;
    private RemindersUI reminders;

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JsonWriter jsonWriterUserInfo;
    private String pathForSpecificUser;
    private static final String JSON_USERINFO = "./data/userinfo.json";
    private User user;
    private String username;

    public HubUI(Boolean isExistingUser, User user) {

        this.user = user;
        initComponents(isExistingUser);
        setupLayout();
        initJson();
        displayHub();
    }

    private void initJson() {
        jsonWriterUserInfo = new JsonWriter(JSON_USERINFO);
        jsonReader = new JsonReader(pathForSpecificUser,JSON_USERINFO);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        add(hubTabs, BorderLayout.CENTER);


    }

    private void initComponents(Boolean isExistingUser) {
        if (isExistingUser) {

            hubTabs = new JTabbedPane();
            int option = JOptionPane.showConfirmDialog(null,
                    "Do you want to reload your saved hub from file?"
                            +
                            " If you choose not to, a new empty hub will be generated for you!",
                    "Secure Hub Login", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                loadHubFromFile(user);
                hubTabs.setVisible(true);
            } else {
                generateNewHub(user);
                JOptionPane.showMessageDialog(null,
                        "Your new empty Hub has been generated!");
            }
            


        } else if (!isExistingUser) {
            hubTabs = new JTabbedPane();
            hubTabs.setVisible(true);
            generateNewHub(user);
            JOptionPane.showMessageDialog(null,
                    "Your new empty Hub has been generated!");
        }


    }

    private void displayHub() {
        notes = new NotesUI(user);
        reminders = new RemindersUI(user);
        hubTabs.addTab("Notes", notes);
        hubTabs.addTab("Reminders", reminders);
        hubTabs.setVisible(true);



    }

    private void generateNewHub(User user) {
        Hub userHub = new Hub();
        user.setHub(userHub);
    }

    private void loadHubFromFile(User user) {
        try {
            String uname = user.getUsername();
            pathForSpecificUser = "./data/" + uname + ".json";
            jsonReader = new JsonReader(pathForSpecificUser,JSON_USERINFO);
            user.setHub(jsonReader.read(user.getUserID()));
            JOptionPane.showMessageDialog(this, "Loaded " + user.getUsername()
                    +
                    "'s Hub from" + pathForSpecificUser);
        } catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(this,
                    "Error! Unable to read from file: " + pathForSpecificUser);
        }
    }
}
