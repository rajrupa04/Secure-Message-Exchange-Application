package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import model.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.HashMap;

public class RemindersUI extends JPanel {
    private User user;
    private JFrame frame;
    private JTable remindersTable;
    private JButton searchButton;
    private JButton viewAllButton;
    private JButton addButton;
    private JButton editReminder;
    private JTextField searchField;
    private Reminder userReminders;

    public RemindersUI(User u) {
        this.user = u;
        initComponents();
        setupLayout();

    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        searchImplementation();
        viewAllImplementation();
        addReminderImplementation();
        editReminderImplementation();

        JScrollPane scrollPane = new JScrollPane(remindersTable);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(viewAllButton);
        buttonsPanel.add(addButton);

        add(buttonsPanel, BorderLayout.SOUTH);


        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void editReminderImplementation() {
    }

    private void addReminderImplementation() {
        addButton = new JButton("Add New Reminder");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewReminder();
            }
        });

    }

    private void addNewReminder() {
        JDialog newReminderDialog = new JDialog(frame, "Add new Reminder");
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField reminderDateField = new JTextField("New Reminder Date", 20);
        JTextField reminderContentField = new JTextField("New Reminder Content", 20);

        newReminderDialog.add(reminderDateField);
        newReminderDialog.add(reminderContentField);
        JButton addInDialog = new JButton("Add");
        newReminderDialog.add(addInDialog);
        addActionListenerToAddInDialog(addInDialog, reminderDateField, reminderContentField, newReminderDialog);

        panel.add(new JLabel("Note ID:"));
        panel.add(reminderDateField);
        panel.add(new JLabel("Note Content:"));
        panel.add(reminderContentField);
        panel.add(new JLabel());
        panel.add(addInDialog);

        newReminderDialog.add(panel);
        newReminderDialog.pack();
        newReminderDialog.setLocationRelativeTo(null);
        newReminderDialog.setVisible(true);
    }

    private void addActionListenerToAddInDialog(JButton addInDialog, JTextField reminderDateField,
                                                JTextField reminderContentField, JDialog newReminderDialog) {

        addInDialog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate d = LocalDate.parse(reminderDateField.getText());
                String reminderContent = reminderContentField.getText();
                userReminders.addNewReminder(d,reminderContent);
                DefaultTableModel tableModel = (DefaultTableModel) remindersTable.getModel();
                tableModel.addRow(new Object[]{d, reminderContent});


                newReminderDialog.dispose();
                ((TableRowSorter<DefaultTableModel>) remindersTable.getRowSorter()).setRowFilter(null);
            }
        });
    }

    private void viewAllImplementation() {
        viewAllButton = new JButton("View All Reminders");
        viewAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((TableRowSorter<DefaultTableModel>) remindersTable.getRowSorter()).setRowFilter(null);
            }
        });

    }

    private void searchImplementation() {
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchReminders();
            }
        });

        JPanel searchPanel = new JPanel();
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(remindersTable);
        add(scrollPane, BorderLayout.CENTER);

    }

    private void searchReminders() {
        String searchText = searchField.getText();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) remindersTable.getModel());
        remindersTable.setRowSorter(sorter);
        if (!searchText.isEmpty()) {
            sorter.setRowFilter(RowFilter.regexFilter(searchText));
        } else {
            sorter.setRowFilter(null);
        }
    }

    private void initComponents() {
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Reminder Date", "Reminder Content"}
        );
        userReminders = user.getHub().getReminder();
        HashMap<LocalDate, String> userNotesHashMap = userReminders.getAllReminders();

        for (LocalDate d : userNotesHashMap.keySet()) {
            tableModel.addRow(new Object[]{d,userNotesHashMap.get(d)});

        }

        remindersTable = new JTable(tableModel);

    }

}
