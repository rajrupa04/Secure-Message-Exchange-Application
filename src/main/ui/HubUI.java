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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

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
    private JPanel savePanel;
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
        savePanel = new JPanel();
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveHubToFile();
            }
        });

        savePanel.add(saveButton);
        add(savePanel, BorderLayout.SOUTH);

    }

    private void saveHubToFile() {
        try {
            if (user != null && user.getUsername() != null) {
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
                    "Unexpected IOException!");
        }
    }

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


        hubTabs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    hubJson[0] = jsonReader.returnJsonObject("./data/" + user.getUsername() + ".json");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null,"IOException!");
                }
                if (hubTabs.getSelectedIndex() == 3) {
                    displayNotifications(hubJson[0]);
                }
            }
        });



    }

    private void displayNotifications(JSONObject hubJson) {
        JSONArray notifsJson = hubJson.getJSONObject("Hub").getJSONArray("Notifications");
        if (notifsJson.length() > 0) {

            for (Object o : notifsJson) {
                JSONObject jsonObj = (JSONObject) o;
                String message = jsonObj.getString("Message");
                JOptionPane.showMessageDialog(null, message);
            }

        }
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
