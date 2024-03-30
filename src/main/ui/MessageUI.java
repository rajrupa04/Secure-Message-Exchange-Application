package ui;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.JsonReader;
import persistence.JsonWriter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

//This class represents the Messages Tab in the user's hub. It handles the displaying, searching and sending
//of messages.

//CITATIONS: The usage of the regexFilter to filter the different messages of the hub was referenced from
// https://docs.oracle.com/en/java/javase/11/docs/api/java.desktop/javax/swing/RowFilter.html

public class MessageUI extends JPanel {
    private JFrame frame;
    private User user;
    private MessageFolder messageFolder;
    private JTable messagesTable;
    private JButton sendMessageButton;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JsonWriter jsonWriterUserInfo;
    private String pathForSpecificUser;
    private static final String JSON_USERINFO = "./data/userinfo.json";
    private JTextField searchField;
    private JButton searchButton;
    private EventLog eventLog;

    //MODIFIES: this
    //EFFECTS: Initializes the MessageUI object, sets up the layout and initializes components.
    public MessageUI(User user) {
        eventLog = EventLog.getInstance();
        this.user = user;
        pathForSpecificUser = "./data/" + user.getUsername() + ".json";
        this.messageFolder = user.getHub().getMessageFolder();
        jsonWriterUserInfo = new JsonWriter(JSON_USERINFO);
        jsonReader = new JsonReader(pathForSpecificUser, JSON_USERINFO);
        initComponents();
        setupLayout();
    }


    //EFFECTS: Configures the layout of the message UI,
    //adds a button to send a new message and a table to display the existing messages.
    private void setupLayout() {
        setLayout(new BorderLayout());
        searchImplementation();
        sendMessageImplementation();

        JScrollPane scrollPane = new JScrollPane(messagesTable);
        add(scrollPane, BorderLayout.CENTER);
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(sendMessageButton);
        add(buttonsPanel, BorderLayout.SOUTH);
        setVisible(true);
    }


    //MODIFIES: this
    //EFFECTS: Defines the functionality for sending a new message. Adds a new button for the same.
    private void sendMessageImplementation() {

        sendMessageButton = new JButton("Send new Message");
        sendMessageButton.addActionListener(new ActionListener() {
            @Override
            //EFFECTS: when the "Send new message" button is clicked, a dialog is presented so that the user
            //can enter the details of the message and recipient.
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

    }

    //EFFECTS: creates a dialog for sending
    // a message, taking in the user input for recipient, urgency level and message content.
    private void sendMessage() {

        Dialog newMessageDialog = new JDialog(frame, "Send new Message");
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField recipientUserId = new JTextField("Recipient User ID", 20);
        JComboBox urgencyComboBox = new JComboBox<>(new String[]{"REGULAR", "URGENT", "EMERGENCY"});
        JTextField msgContents = new JTextField("Message Contents", 45);

        newMessageDialog.add(recipientUserId);
        newMessageDialog.add(urgencyComboBox);
        JButton send = new JButton("Send");
        newMessageDialog.add(send);
        addActionListenerToSendInDialog(send, recipientUserId, urgencyComboBox, msgContents, newMessageDialog);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Enter the user ID of the recipient."));
        panel.add(recipientUserId);
        panel.add(new JLabel("State the urgency level of this message."));
        panel.add(urgencyComboBox);
        panel.add(new JLabel("Enter the contents of the message you'd like to send."));
        panel.add(msgContents);
        panel.add(new JLabel());
        panel.add(send);

        newMessageDialog.add(panel);
        newMessageDialog.pack();
        newMessageDialog.setLocationRelativeTo(null);
        newMessageDialog.setVisible(true);
    }

    //EFFECTS: adds an action listener to the send button in the dialog presented to the user.
    private void addActionListenerToSendInDialog(JButton send, JTextField recipientUserId,
                                                 JComboBox urgencyComboBox, JTextField msgContents,
                                                 Dialog newMessageDialog) {

        User sender = this.user;
        send.addActionListener(new ActionListener() {
            @Override
            //EFFECTS: When the send button in the dialog is clicked, the system retrieves the necessary information
            // from the input fields, sends the message, and updates the UI accordingly. Appropriate error messages
            // are displayed if an exception is caught.
            public void actionPerformed(ActionEvent e) {
                Integer rid = Integer.parseInt(recipientUserId.getText());
                User recipient = readUserFromFile(rid);
                String recipientFilePath = "./data/" + recipient.getUsername() + ".json";
                try {
                    loadRecipientHub(recipient, recipientFilePath);
                    UrgencyLevel ul = UrgencyLevel.valueOf(urgencyComboBox.getSelectedItem().toString());
                    String msg = msgContents.getText();
                    Integer messageID = 0;
                    messageID = sendMessageToRecipient(messageID, sender, recipient, msg, ul);
                    addMessageToRecipientJson(recipient, messageID, sender, msg, ul, recipientFilePath);
                } catch (NoSuchPaddingException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Error! Unexpected NoSuchPaddingException");
                } catch (NoSuchAlgorithmException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Error! Unexpected NoSuchAlgorithmException");
                }


            }
        });
    }

    //EFFECTS: Handles the sending of the message from the specified sender to the recipient. Shows appropriate
    //error messages if exceptions are encountered. Returns and displays the messageID of the message.
    private Integer sendMessageToRecipient(Integer messageID, User sender, User recipient, String msg,
                                           UrgencyLevel ul) {

        try {
            messageID = sender.getHub().sendMessage(sender, recipient, msg, ul);
            JOptionPane.showMessageDialog(null,
                    "Message sent! The message ID is: " + messageID);
            this.user = sender;
        } catch (UnsupportedEncodingException e) {
            JOptionPane.showMessageDialog(null,"Unexpected UnsupportedEncodingException!");
        } catch (IllegalBlockSizeException e) {
            JOptionPane.showMessageDialog(null,"Unexpected IllegalBlockSizeException!");
        } catch (BadPaddingException e) {
            JOptionPane.showMessageDialog(null,"Unexpected BadPaddingException!");
        } catch (InvalidKeyException e) {
            JOptionPane.showMessageDialog(null,"Unexpected InvalidKeyException!");
        } catch (NoSuchPaddingException e) {
            JOptionPane.showMessageDialog(null,"Unexpected NoSuchPaddingException!");
        } catch (NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(null,"Unexpected NoSuchAlgorithmException!");
        }
        return messageID;
    }


    //EFFECTS: This method adds the message which has been sent to the recipient's JSON file. Displays an
    //appropriate message if an IOException is encountered.
    private void addMessageToRecipientJson(User recipient, Integer messageID, User sender,
                                           String msgContents, UrgencyLevel msgUrgency, String recipientFilePath) {

        jsonReader = new JsonReader(recipientFilePath, JSON_USERINFO);
        try {
            JSONObject hubJson = jsonReader.returnJsonObject(recipientFilePath);
            JSONArray messageFolderArray = hubJson.getJSONObject("Hub").getJSONArray("MessageFolder");
            JSONObject msg = new JSONObject();
            msg.put("SenderUserID", sender.getUserID());
            msg.put("RecipientUserID", recipient.getUserID());
            msg.put("MessageID", messageID);
            msg.put("DecryptedMessageText", msgContents);
            msg.put("Urgency Level", msgUrgency.toString());
            messageFolderArray.put(msg);
            hubJson.getJSONObject("Hub").put("MessageFolder", messageFolderArray);


            jsonWriter = new JsonWriter(recipientFilePath);
            jsonWriter.open();
            jsonWriter.writeHub(recipient.getUsername(),
                    recipient.getUserID().toString(), recipient.getHub());
            jsonWriter.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error! Unable to write to recipient file");
        }
    }


    //MODIFIES: this
    //EFFECTS: This method loads the recipient's hub from their specific file, so that the message can be added.
    //Displays an appropriate message if an IOException is encountered.
    private void loadRecipientHub(User recipient, String fp) throws NoSuchPaddingException, NoSuchAlgorithmException {
        try {
            jsonReader = new JsonReader(fp, JSON_USERINFO);
            Hub h = new Hub();
            h = jsonReader.read(user.getUserID());
            recipient.setHub(h);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error! Unable to read from file: " + pathForSpecificUser);
        }

    }

    //MODIFIES: this
    //EFFECTS: Returns the user corresponding to the provided userID. Displays an appropriate message if an
    // IOException is encountered.
    private User readUserFromFile(Integer id) {
        try {

            JSONObject userJsonObject = jsonReader.returnJsonObject(JSON_USERINFO);
            User u = jsonReader.getUserByID(id, userJsonObject);
            return u;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error retrieving user from file: " + JSON_USERINFO);
        }
        return null;
    }


    //MODIFIES: this
    //EFFECTS: Implements message search functionality, allowing users to search for specific messages based on
    //message content.
    private void searchImplementation() {
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            //EFFECTS: When the search button is clicked, the message are filtered based on the text provided in the
            //search field.
            public void actionPerformed(ActionEvent e) {
                searchMessages();
            }
        });

        JPanel searchPanel = new JPanel();
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(messagesTable);
        add(scrollPane, BorderLayout.CENTER);

    }

    //MODIFIES: this
    //EFFECTS: Performs the search operation based on the text typed into the search field. Precautions are
    //taken so that the system treats special characters normally and not as characters which serve special meanings in
    //Regex.
    private void searchMessages() {
        String searchText = searchField.getText();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) messagesTable.getModel());
        messagesTable.setRowSorter(sorter);
        if (!searchText.isEmpty()) {
            String escapedSearchText = Pattern.quote(searchText);
            sorter.setRowFilter(RowFilter.regexFilter(escapedSearchText));
        } else {
            sorter.setRowFilter(null);
        }
    }

    //MODIFIES: this
    //EFFECTS: Initializes the table in which the messages are to be displayed.
    private void initComponents() {
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[][][]{},
                new String[]{"Sender", "Urgency Level", "Message"}
        );

        JSONObject hubJson = new JSONObject();
        fillInMessageTable(hubJson, tableModel);


        messagesTable = new JTable(tableModel);
        messagesTable.setVisible(true);
    }

    //MODIFIES: this
    //EFFECTS: This method takes the messages saved on file and adds them to the table in the Hub UI. Displays an
    // appropriate message if an IOException is encountered.
    private void fillInMessageTable(JSONObject hubJson, DefaultTableModel tableModel) {
        try {
            if (new File(pathForSpecificUser).exists()) {
                hubJson = jsonReader.returnJsonObject(pathForSpecificUser);
                JSONArray messageFolder = hubJson.getJSONObject("Hub").getJSONArray("MessageFolder");
                for (Object o : messageFolder) {
                    JSONObject jsonObj = (JSONObject) o;
                    String decryptedMessage = jsonObj.getString("DecryptedMessageText");
                    Integer senderID = jsonObj.getInt("SenderUserID");
                    JSONObject userJsonObject = jsonReader.returnJsonObject(JSON_USERINFO);
                    String senderUsername = jsonReader.getUserByID(senderID, userJsonObject).getUsername();
                    String urgency = jsonObj.getString("Urgency Level");
                    tableModel.addRow(new Object[]{senderUsername, urgency, decryptedMessage});
                }
            }


        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unexpected IOException in MessageUI!");
        }
    }
}




