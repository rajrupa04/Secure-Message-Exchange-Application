package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import model.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.regex.Pattern;

//This class represents the Reminders tab of the user's hub, managing the searching, viewing and adding of reminders.
//CITATIONS: The usage of the regexFilter to filter the different reminders was referenced from
// https://docs.oracle.com/en/java/javase/11/docs/api/java.desktop/javax/swing/RowFilter.html

public class RemindersUI extends JPanel {
    private User user;
    private JFrame frame;
    private JTable remindersTable;
    private JButton searchButton;
    private JButton viewAllButton;
    private JButton addButton;
    private JTextField searchField;
    private Reminder userReminders;

    //MODIFIES: this
    //EFFECTS: Constructs a new instance of RemindersUI. Initiates the components and sets up layout of those
    // components.
    public RemindersUI(User u) {
        this.user = u;
        initComponents();
        setupLayout();

    }

    //EFFECTS: Configures the layout and adds the necessary buttons to the tab. Ensures all the elements are visible.
    private void setupLayout() {
        setLayout(new BorderLayout());
        searchImplementation();
        viewAllImplementation();
        addReminderImplementation();

        JScrollPane scrollPane = new JScrollPane(remindersTable);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(viewAllButton);
        buttonsPanel.add(addButton);

        add(buttonsPanel, BorderLayout.SOUTH);


        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    //MODIFIES: this
    //EFFECTS: Initializes the add button with the action listener to add a new reminder.
    private void addReminderImplementation() {
        addButton = new JButton("Add New Reminder");
        addButton.addActionListener(new ActionListener() {
            @Override
            //EFFECTS: when the add button is clicked, a dialog is presented for the user to enter the date and content
            //of the new reminder.
            public void actionPerformed(ActionEvent e) {
                addNewReminder();
            }
        });

    }

    //EFFECTS: Sets up and displays the dialog asking the user to enter the date and contents of the new reminder.
    private void addNewReminder() {
        JDialog newReminderDialog = new JDialog(frame, "Add new Reminder");
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField reminderDateField = new JTextField("yyyy-mm-dd", 20);
        JTextField reminderContentField = new JTextField("New Reminder Content", 20);

        newReminderDialog.add(reminderDateField);
        newReminderDialog.add(reminderContentField);
        JButton addInDialog = new JButton("Add");
        newReminderDialog.add(addInDialog);
        addActionListenerToAddInDialog(addInDialog, reminderDateField, reminderContentField, newReminderDialog);

        panel.add(new JLabel("Date of reminder (yyyy-mm-dd) :"));
        panel.add(reminderDateField);
        panel.add(new JLabel("Reminder Content:"));
        panel.add(reminderContentField);
        panel.add(new JLabel());
        panel.add(addInDialog);

        newReminderDialog.add(panel);
        newReminderDialog.pack();
        newReminderDialog.setLocationRelativeTo(null);
        newReminderDialog.setVisible(true);
    }

    //MODIFIES: this
    //EFFECTS: Adds the action listener to the add button in the dialog.
    private void addActionListenerToAddInDialog(JButton addInDialog, JTextField reminderDateField,
                                                JTextField reminderContentField, JDialog newReminderDialog) {

        addInDialog.addActionListener(new ActionListener() {
            @Override
            //EFFECTS: When the add button in the dialog is clicked, the new reminder is added to the table as well as
            //to the HashMap of the user's reminders.
            public void actionPerformed(ActionEvent e) {
                LocalDate d = LocalDate.parse(reminderDateField.getText());
                String reminderContent = reminderContentField.getText();
                userReminders.addNewReminder(d,reminderContent);
                DefaultTableModel tableModel = (DefaultTableModel) remindersTable.getModel();
                tableModel.addRow(new Object[]{d, reminderContent});


                newReminderDialog.dispose();
                if (remindersTable.getRowSorter() != null) {
                    ((TableRowSorter<DefaultTableModel>) remindersTable.getRowSorter()).setRowFilter(null);
                }
            }
        });
    }

    //MODIFIES: this
    //EFFECTS: Provides the implementation for viewing all the reminders.
    private void viewAllImplementation() {
        viewAllButton = new JButton("View All Reminders");
        viewAllButton.addActionListener(new ActionListener() {
            @Override
            //EFFECTS: When the view all reminders button is clicked, all the search filters are reset so that
            //all the reminders are visible.
            public void actionPerformed(ActionEvent e) {
                if (remindersTable.getRowSorter() != null) {
                    ((TableRowSorter<DefaultTableModel>) remindersTable.getRowSorter()).setRowFilter(null);
                }
            }
        });

    }

    //MODIFIES: this
    //EFFECTS: Provides the implementation of searching reminders, initialises the search bar and button.
    private void searchImplementation() {
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            //EFFECTS: When the search button is clicked, the reminders are filtered according to their content.
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

    //MODIFIES: this
    //EFFECTS: Filters the reminders table based on the entered search text. Special care is taken such that special
    //characters like emoticons are included in the search text and not treated as Regex special characters.
    private void searchReminders() {
        String searchText = searchField.getText();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) remindersTable.getModel());
        remindersTable.setRowSorter(sorter);
        if (!searchText.isEmpty()) {
            String escapedSearchText = Pattern.quote(searchText);
            sorter.setRowFilter(RowFilter.regexFilter(escapedSearchText));
        } else {
            sorter.setRowFilter(null);
        }
    }

    //MODIFIES: this
    //EFFECTS: Sets up and displays the reminder table. Adds the entries in the user's reminders to the table.
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
