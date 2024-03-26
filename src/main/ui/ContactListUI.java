package ui;


import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class ContactListUI extends JPanel {
    private User user;
    private JFrame frame;
    private JTable contactsTable;
    private JButton addButton;
    private ArrayList userContacts;
    private JButton deleteButton;

    public ContactListUI(User u) {
        this.user = u;
        userContacts = new ArrayList<>();
        initComponents();
        setupLayout();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        addContactImplementation();
        deleteContactImplementation();

        JScrollPane scrollPane = new JScrollPane(contactsTable);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(addButton);
        buttonsPanel.add(deleteButton);

        add(buttonsPanel, BorderLayout.SOUTH);

        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void deleteContactImplementation() {
        deleteButton = new JButton("Delete Existing Contact");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteExistingContact();
            }
        });
    }

    private void deleteExistingContact() {
        JDialog deleteDialog = new JDialog(frame, "Delete Existing Contact");
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField usernameField = new JTextField("Username", 20);

        deleteDialog.add(usernameField);
        JButton deleteInDialog = new JButton("Delete");
        deleteDialog.add(deleteInDialog);
        addActionListenerToDeleteDialog(deleteDialog, usernameField, deleteInDialog);

        panel.add(new JLabel("Username of contact to be deleted:"));
        panel.add(usernameField);
        panel.add(new JLabel());
        panel.add(deleteInDialog);

        deleteDialog.add(panel);
        deleteDialog.pack();
        deleteDialog.setLocationRelativeTo(null);
        deleteDialog.setVisible(true);
    }

    private void addActionListenerToDeleteDialog(JDialog deleteDialog, JTextField usernameField,
                                                 JButton deleteInDialog) {

        deleteInDialog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                userContacts.remove(username);
                DefaultTableModel tableModel = (DefaultTableModel) contactsTable.getModel();
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    if (tableModel.getValueAt(i, 1).toString().equals(username)) {
                        tableModel.removeRow(i);
                        break;
                    }
                }


                deleteDialog.dispose();
                if (contactsTable.getRowSorter() != null) {
                    ((TableRowSorter<DefaultTableModel>) contactsTable.getRowSorter()).setRowFilter(null);
                }
            }
        });
    }

    private void addContactImplementation() {
        addButton = new JButton("Add New Contact");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewContact();
            }
        });
    }

    private void addNewContact() {
        JDialog newContactPanel = new JDialog(frame, "Add new Contact");
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField contactUsernameField = new JTextField("user123", 20);

        newContactPanel.add(contactUsernameField);
        JButton addInDialog = new JButton("Add");
        newContactPanel.add(addInDialog);
        addActionListenerToAddInDialog(addInDialog, contactUsernameField, newContactPanel);

        panel.add(new JLabel("Username of new contact:"));
        panel.add(contactUsernameField);
        panel.add(new JLabel());
        panel.add(addInDialog);

        newContactPanel.add(panel);
        newContactPanel.pack();
        newContactPanel.setLocationRelativeTo(null);
        newContactPanel.setVisible(true);
    }

    private void addActionListenerToAddInDialog(JButton addInDialog, JTextField contactUsernameField,
                                                JDialog newContactPanel) {

        addInDialog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = contactUsernameField.getText();
                userContacts.add(username);
                DefaultTableModel tableModel = (DefaultTableModel) contactsTable.getModel();
                tableModel.addRow(new Object[]{userContacts.size(), username});


                newContactPanel.dispose();

            }
        });

    }



    private void initComponents() {
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Serial Number", "Username of Contact"}
        );
        userContacts = user.getHub().getContactList();

        for (int i = 0; i < userContacts.size(); i++) {
            tableModel.addRow(new Object[]{(i + 1),userContacts.get(i)});

        }

        contactsTable = new JTable(tableModel);

    }


}
