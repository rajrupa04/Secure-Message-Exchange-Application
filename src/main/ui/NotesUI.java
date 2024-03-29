package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import model.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.regex.Pattern;

//This class manages the Notes tab in the user's hub, working with adding, searching, viewing and deleting notes.

//CITATIONS: The usage of the regexFilter to filter the different notes was referenced from
// https://docs.oracle.com/en/java/javase/11/docs/api/java.desktop/javax/swing/RowFilter.html


public class NotesUI extends JPanel {
    private JFrame frame;
    private JTable notesTable;
    private User user;
    private JButton searchButton;
    private JButton viewAllButton;
    private JButton addButton;
    private JButton deleteButton;
    private JTextField searchField;
    private Note userNotes;


    //MODIFIES: this
    //EFFECTS: Constructs a new instance of NotesUI. Initiates the components and sets up layout of those components.
    public NotesUI(User u) {

        this.user = u;
        initComponents();
        setupLayout();

    }

    //MODIFIES: this
    //EFFECTS: Creates and adds the notes from the user's hub to the table.
    private void initComponents() {
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Note ID", "Note Content"}
        );
        userNotes = user.getHub().getNote();
        HashMap<Integer, String> userNotesHashMap = userNotes.getListOfNotes();

        for (Integer i : userNotesHashMap.keySet()) {
            tableModel.addRow(new Object[]{i,userNotesHashMap.get(i)});

        }

        notesTable = new JTable(tableModel);
    }

    //EFFECTS: Configures the layout and adds the required buttons to the UI.
    private void setupLayout() {
        setLayout(new BorderLayout());
        searchImplementation();
        viewAllImplementation();
        addNoteImplementation();
        deleteNoteImplementation();

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(viewAllButton);
        buttonsPanel.add(addButton);
        buttonsPanel.add(deleteButton);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    //MODIFIES: this
    //EFFECTS: Provides the implementation for deleting an existing note.
    private void deleteNoteImplementation() {
        deleteButton = new JButton("Delete Existing Note");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            //EFFECTS: When the delete existing note button is clicked, a dialog is presented to the user
            //prompting them to enter the Note ID of the note they want to delete.
            public void actionPerformed(ActionEvent e) {
                deleteExistingNote();
            }
        });
    }

    //EFFECTS: Creates and displays the dialog asking the user to enter the note ID of the note to be deleted.
    private void deleteExistingNote() {
        JDialog deleteDialog = new JDialog(frame, "Delete Existing Note");
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField noteIDField = new JTextField("Note ID of note to be deleted", 20);

        deleteDialog.add(noteIDField);
        JButton deleteInDialog = new JButton("Delete");
        deleteDialog.add(deleteInDialog);
        addActionListenerToDeleteDialog(deleteDialog, noteIDField, deleteInDialog);

        panel.add(new JLabel("Note ID of note to be deleted:"));
        panel.add(noteIDField);
        panel.add(new JLabel());
        panel.add(deleteInDialog);

        deleteDialog.add(panel);
        deleteDialog.pack();
        deleteDialog.setLocationRelativeTo(null);
        deleteDialog.setVisible(true);
    }

    //MODIFIES: this
    //EFFECTS: Adds an action listener to the delete button present in the dialog.
    private void addActionListenerToDeleteDialog(JDialog deleteDialog, JTextField noteIDField, JButton deleteInDialog) {
        deleteInDialog.addActionListener(new ActionListener() {
            @Override
            //EFFECTS: When the delete button in the dialog is clicked, the note corresponding to the provided noteID
            //is removed from the user's notes as well as the table.
            public void actionPerformed(ActionEvent e) {
                Integer noteID = Integer.parseInt(noteIDField.getText());
                userNotes.removeNote(noteID);
                DefaultTableModel tableModel = (DefaultTableModel) notesTable.getModel();
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    if (Integer.parseInt(tableModel.getValueAt(i, 0).toString()) == noteID) {
                        tableModel.removeRow(i);
                        break;
                    }
                }


                deleteDialog.dispose();
                viewAllNotes();
            }
        });

    }

    //MODIFIES: this
    //EFFECTS: Provides the implementation for adding a new note.
    private void addNoteImplementation() {
        addButton = new JButton("Add New Note");
        addButton.addActionListener(new ActionListener() {
            @Override
            //EFFECTS: When the add new note button is clicked, a dialog is displayed asking the user to enter the
            //new note ID and content.
            public void actionPerformed(ActionEvent e) {
                addNewNote();
            }
        });
    }

    //MODIFIES: this
    //EFFECTS: Provides the implementation for viewing all notes.
    private void viewAllImplementation() {
        viewAllButton = new JButton("View All Notes");
        viewAllButton.addActionListener(new ActionListener() {
            @Override
            //EFFECTS: When the view all notes button is clicked, the search filter is reset.
            public void actionPerformed(ActionEvent e) {
                viewAllNotes();
            }
        });
    }

    //MODIFIES: this
    //EFFECTS: Provides the implementation for searching for notes based on note content. Sets up the search field
    //and panel.
    private void searchImplementation() {
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            //EFFECTS: When the search button is clicked, it filters the notes based on the text in the search field.
            public void actionPerformed(ActionEvent e) {
                searchNotes();
            }
        });

        JPanel searchPanel = new JPanel();
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(notesTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    //EFFECTS: Displays and sets up the dialog for adding a new note, asking the user to enter the ID and content
    //of the new note.
    private void addNewNote() {
        JDialog newNoteDialog = new JDialog(frame, "Add new note");
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField noteIDField = new JTextField("New Note ID", 20);
        JTextField noteContentField = new JTextField("New Note Content", 20);

        newNoteDialog.add(noteIDField);
        newNoteDialog.add(noteContentField);
        JButton addInDialog = new JButton("Add");
        newNoteDialog.add(addInDialog);
        addActionListenerToAddInDialog(addInDialog, noteIDField, noteContentField, newNoteDialog);

        panel.add(new JLabel("Note ID:"));
        panel.add(noteIDField);
        panel.add(new JLabel("Note Content:"));
        panel.add(noteContentField);
        panel.add(new JLabel());
        panel.add(addInDialog);

        newNoteDialog.add(panel);
        newNoteDialog.pack();
        newNoteDialog.setLocationRelativeTo(null);
        newNoteDialog.setVisible(true);

    }

    //MODIFIES: this
    //EFFECTS: Adds an action listener to the add button in the dialog.
    private void addActionListenerToAddInDialog(JButton addInDialog, JTextField noteIDField,
                                                JTextField noteContentField, JDialog newNoteDialog) {
        addInDialog.addActionListener(new ActionListener() {
            @Override
            //EFFECTS: if the add button in the dialog is clicked, a new note with the provided note ID and content
            //is added to the user's notes and also the table.
            public void actionPerformed(ActionEvent e) {
                Integer noteID = Integer.parseInt(noteIDField.getText());
                String noteContent = noteContentField.getText();
                userNotes.addNote(noteID,noteContent);
                DefaultTableModel tableModel = (DefaultTableModel) notesTable.getModel();
                tableModel.addRow(new Object[]{noteID, noteContent});


                newNoteDialog.dispose();
                viewAllNotes();
            }
        });
    }

    //MODIFIES: this
    //EFFECTS: Clears any applied filters and displays all the notes in the table.
    private void viewAllNotes() {
        if (notesTable.getRowSorter() != null) {
            ((TableRowSorter<DefaultTableModel>) notesTable.getRowSorter()).setRowFilter(null);
        }
    }

    //MODIFIES: this
    //EFFECTS: Filters the notes table based on the entered search text. Special care is taken such that special
    //characters like emoticons are included in the search text and not treated as Regex special characters.
    private void searchNotes() {
        String searchText = searchField.getText();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) notesTable.getModel());
        notesTable.setRowSorter(sorter);
        if (!searchText.isEmpty()) {
            String escapedSearchText = Pattern.quote(searchText);
            sorter.setRowFilter(RowFilter.regexFilter(escapedSearchText));
        } else {
            sorter.setRowFilter(null);
        }
    }


}



