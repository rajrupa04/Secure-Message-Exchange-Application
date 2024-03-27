package ui;

import model.Hub;
import model.User;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.JsonReader;
import persistence.JsonWriter;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

//This class represents the main hub user interface, allowing the user to manage notes, reminders, contacts, messages
// and notifications.

public class HubUI extends JPanel {
    private JTabbedPane hubTabs;
    private NotesUI notes;
    private RemindersUI reminders;
    private ContactListUI contactList;
    private MessageUI messages;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JsonWriter jsonWriterUserInfo;
    private String pathForSpecificUser;
    private static final String JSON_USERINFO = "./data/userinfo.json";
    private User user;
    private JButton quitWithoutSavingButton;
    private boolean notificationsDisplayed;


    //MODIFIES: this
    //EFFECTS: Constructs a new HubUI object. Initializes components, sets up layout,
    // initializes JSON reader and writer, and displays the hub, while keeping track of whether the notification
    // has been displayed.
    public HubUI(Boolean isExistingUser, User user) {

        this.user = user;
        notificationsDisplayed = false;
        initComponents(isExistingUser);
        setupLayout();
        initJson();
        displayHub();
    }

    //EFFECTS: Initialises the JSON Writer to write to the file containing all user information, and the reader to
    //read from both the aforementioned file and the file pertaining to the current user's hub.
    private void initJson() {
        jsonWriterUserInfo = new JsonWriter(JSON_USERINFO);
        jsonReader = new JsonReader(pathForSpecificUser,JSON_USERINFO);
    }

    //EFFECTS: sets up the layout of the hub, adding the hub tabbed pane to the frame along with the quit and save
    //buttons.
    private void setupLayout() {
        setLayout(new BorderLayout());
        add(hubTabs, BorderLayout.CENTER);
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            //EFFECTS: When the save button is clicked, the hub is saved to the JSON file for the current user.
            public void actionPerformed(ActionEvent e) {
                saveHubToFile();
            }
        });

        quitWithoutSavingButton = new JButton("Quit without saving");
        quitWithoutSavingButton.addActionListener(new ActionListener() {
            @Override
            //EFFECTS: When the quit without saving button is clicked,
            // the program exits without saving the user's progress.
            public void actionPerformed(ActionEvent e) {
                quitWithoutSavingImplementation();
                }


        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(saveButton);
        buttonPanel.add(quitWithoutSavingButton);
        add(buttonPanel,BorderLayout.SOUTH);


    }

    //EFFECTS: Asks the user if they are sure they want to quit without saving. If they choose yes, the program quits.
    private void quitWithoutSavingImplementation() {
        int confirmed = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to quit without saving your hub?", "Quit Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    //EFFECTS: Writes the current hub data to a JSON file. Displays appropriate messages when an IOException or
    // FileNotFoundException is encountered.
    private void saveHubToFile() {
        try {
            if (user != null && user.getUsername() != null) {
                pathForSpecificUser = "./data/" + user.getUsername() + ".json";
                jsonWriter = new JsonWriter(pathForSpecificUser);
                jsonWriter.open();
                jsonWriter.writeHub(user.getUsername(), user.getUserID().toString(), user.getHub());
                jsonWriter.close();
                displaySavedHubLogo();
            } else {
                JOptionPane.showMessageDialog(null,
                        "User is not properly initialized or username is null.");
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "Unexpected FileNotFoundException!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Unexpected IOException during saving!");
        }
    }

    //EFFECTS: displays the image pertaining to the hub being successfully saved to file. The image is added to
    // a panel along with a message.
    private void displaySavedHubLogo() {
        UIManager.put("OptionPane.minimumSize", new Dimension(600, 600));
        ImageIcon imageIcon = new ImageIcon("./data/hub_saved_icon.png");
        Image originalImage = imageIcon.getImage();
        int desiredWidth = 500;
        int desiredHeight = 500;
        Image scaledImage = originalImage.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JPanel panel = new JPanel(new BorderLayout());
        JLabel imageLabel = new JLabel(scaledIcon);
        panel.add(imageLabel, BorderLayout.WEST);
        JLabel text = new JLabel("Saved " + user.getUsername() + "'s Hub to" + pathForSpecificUser);
        panel.add(text,BorderLayout.SOUTH);
        JOptionPane.showMessageDialog(null, panel, "Saved", JOptionPane.INFORMATION_MESSAGE);
        UIManager.put("OptionPane.minimumSize", new Dimension(400, 150));

    }

    //EFFECTS: If the user has an existing account, then the method loads the user's hub from file if the user wants to,
    //otherwise generates an empty hub. Initialises the user's specific file path as needed.
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
            pathForSpecificUser = "./data/" + user.getUsername() + ".json";
            generateNewHub(user);
            JOptionPane.showMessageDialog(null,
                    "Your new empty Hub has been generated!");
        }

    }

    //EFFECTS: Adds tabs for notes, reminders, contacts, and messages to the hub UI and displays it.
    private void displayHub() {
        notes = new NotesUI(user);
        reminders = new RemindersUI(user);
        contactList = new ContactListUI(user);
        messages = new MessageUI(user);

        hubTabs.addTab("Notes", notes);
        hubTabs.addTab("Reminders", reminders);
        hubTabs.addTab("Contact List",contactList);
        hubTabs.addTab("Messages",messages);
        hubTabs.setVisible(true);
        final JSONObject[] hubJson = {null};
        addChangeListenerToHub();



    }

    //EFFECTS: Handles changes in the selected tab of the hub.
    private void addChangeListenerToHub() {
        final JSONObject[] hubJson = {null};
        hubTabs.addChangeListener(new ChangeListener() {
            @Override
            //EFFECTS: If the messages tab in the Hub UI is opened and the user has a new message,
            //a notification is displayed only once.
            public void stateChanged(ChangeEvent e) {
                try {
                    if (new File("./data/" + user.getUsername() + ".json").exists()) {
                        hubJson[0] = jsonReader.returnJsonObject("./data/" + user.getUsername() + ".json");
                    }


                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null,"IOException!");
                }
                if ((hubTabs.getSelectedIndex() == 3) && !notificationsDisplayed) {
                    displayNotifications(hubJson[0]);
                    notificationsDisplayed = true;
                }
            }
        });
    }

    //EFFECTS: Displays the most recent notification (pertaining to the new message)
    private void displayNotifications(JSONObject hubJson) {
        if (hubJson != null) {

            JSONArray notifsJson = hubJson.getJSONObject("Hub").getJSONArray("Notifications");
            int lastIndex = notifsJson.length() - 1;
            if (lastIndex >= 0) {
                JSONObject lastJsonObject = notifsJson.getJSONObject(lastIndex);
                String message = lastJsonObject.getString("Message");
                JOptionPane.showMessageDialog(null, message);

            }

        }

    }

    //EFFECTS: Generates an empty hub and sets it for the current user.
    private void generateNewHub(User user) {
        Hub userHub = new Hub();
        user.setHub(userHub);
    }

    //EFFECTS: Loads the hub data from a JSON file for the existing user. Displays appropriate messages
    //if an exception is encountered.
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
