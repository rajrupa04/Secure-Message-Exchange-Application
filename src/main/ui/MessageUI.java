package ui;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

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

    public MessageUI(User user) {
        this.user = user;
        pathForSpecificUser = "./data/" + user.getUsername() + ".json";
        this.messageFolder = user.getHub().getMessageFolder();
        jsonWriterUserInfo = new JsonWriter(JSON_USERINFO);
        jsonReader = new JsonReader(pathForSpecificUser,JSON_USERINFO);
        initComponents();
        setupLayout();
    }

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


    private void sendMessageImplementation() {

        sendMessageButton = new JButton("Send new Message");
        sendMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

    }

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

    private void addActionListenerToSendInDialog(JButton send, JTextField recipientUserId,
                                                 JComboBox urgencyComboBox, JTextField msgContents,
                                                 Dialog newMessageDialog) {

        User sender = this.user;
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer rid = Integer.parseInt(recipientUserId.getText());
                User recipient = readUserFromFile(rid);
                String recipientFilePath = "./data/" + recipient.getUsername() + ".json";
                try {
                    loadRecipientHub(recipient,recipientFilePath);
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

    private Integer sendMessageToRecipient(Integer messageID, User sender, User recipient, String msg,
                                           UrgencyLevel ul) {

        try {
            messageID = sender.getHub().sendMessage(sender, recipient, msg, ul);
            JOptionPane.showMessageDialog(null,
                    "Message sent! The message ID is: " + messageID);
            this.user = sender;
        } catch (UnsupportedEncodingException e) {
            System.err.println("Unexpected UnsupportedEncodingException!");
        } catch (IllegalBlockSizeException e) {
            System.err.println("Unexpected IllegalBlockSizeException!");
        } catch (BadPaddingException e) {
            System.err.println("Unexpected BadPaddingException!");
        } catch (InvalidKeyException e) {
            System.err.println("Unexpected InvalidKeyException!");
        } catch (NoSuchPaddingException e) {
            System.err.println("Unexpected NoSuchPaddingException!");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Unexpected NoSuchAlgorithmException!");
        }
        return messageID;
    }

    private void addMessageToRecipientJson(User recipient, Integer messageID, User sender,
                                           String msgContents, UrgencyLevel msgUrgency, String recipientFilePath) {

        jsonReader = new JsonReader(recipientFilePath,JSON_USERINFO);
        try {
            JSONObject hubJson = jsonReader.returnJsonObject(recipientFilePath);
            JSONArray messageFolderArray = hubJson.getJSONObject("Hub").getJSONArray("MessageFolder");
            JSONObject msg = new JSONObject();
            msg.put("SenderUserID",sender.getUserID());
            msg.put("RecipientUserID",recipient.getUserID());
            msg.put("MessageID",messageID);
            msg.put("DecryptedMessageText",msgContents);
            msg.put("Urgency Level",msgUrgency.toString());
            messageFolderArray.put(msg);
            hubJson.getJSONObject("Hub").put("MessageFolder", messageFolderArray);


            jsonWriter = new JsonWriter(recipientFilePath);
            jsonWriter.open();
            jsonWriter.writeHub(recipient.getUsername(),
                    recipient.getUserID().toString(), recipient.getHub());
            jsonWriter.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"Error! Unable to write to recipient file");
        }
    }


    private void loadRecipientHub(User recipient, String fp) throws NoSuchPaddingException, NoSuchAlgorithmException {
        try {
            jsonReader = new JsonReader(fp,JSON_USERINFO);
            Hub h = new Hub();
            h = jsonReader.read(user.getUserID());
            recipient.setHub(h);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error! Unable to read from file: " + pathForSpecificUser);
        }

    }

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


    private void searchImplementation() {

    }

    private void initComponents() {
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[][][]{},
                new String[]{"Sender", "Urgency Level", "Message"}
        );

        try {
            JSONObject hubJson = jsonReader.returnJsonObject(pathForSpecificUser);
            JSONArray messageFolder = hubJson.getJSONObject("Hub").getJSONArray("MessageFolder");
            for (Object o : messageFolder) {
                JSONObject jsonObj = (JSONObject) o;
                String decryptedMessage = jsonObj.getString("DecryptedMessageText");
                Integer senderID = jsonObj.getInt("SenderUserID");
                JSONObject userJsonObject =  jsonReader.returnJsonObject(JSON_USERINFO);
                String senderUsername = jsonReader.getUserByID(senderID,userJsonObject).getUsername();
                String urgency = jsonObj.getString("Urgency Level");
                tableModel.addRow(new Object[]{senderUsername,urgency,decryptedMessage});

            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"Unexpected IOException!");
        }

        messagesTable = new JTable(tableModel);
        messagesTable.setVisible(true);

    }

}
