package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import model.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class NotesUI extends JPanel {
    private JFrame frame;
    private JTable notesTable;
    private JTableHeader notesTableheader;
    private User user;
    private JButton searchButton;
    private JButton viewAllButton;
    private JButton addButton;
    private JTextField searchField;
    private Note userNotes;


    public NotesUI(User u) {

        this.user = u;
        initComponents();
        setupLayout();

    }

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

    private void setupLayout() {
        setLayout(new BorderLayout());
        searchImplementation();
        viewAllImplementation();
        addNoteImplementation();

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(viewAllButton);
        buttonsPanel.add(addButton);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void addNoteImplementation() {
        addButton = new JButton("Add New Note");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewNote();
            }
        });
    }

    private void viewAllImplementation() {
        viewAllButton = new JButton("View All Notes");
        viewAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAllNotes();
            }
        });
    }

    private void searchImplementation() {
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
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

    private void addActionListenerToAddInDialog(JButton addInDialog, JTextField noteIDField,
                                                JTextField noteContentField, JDialog newNoteDialog) {
        addInDialog.addActionListener(new ActionListener() {
            @Override
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

    private void viewAllNotes() {
        ((TableRowSorter<DefaultTableModel>) notesTable.getRowSorter()).setRowFilter(null);
    }

    private void searchNotes() {
        String searchText = searchField.getText();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) notesTable.getModel());
        notesTable.setRowSorter(sorter);
        if (!searchText.isEmpty()) {
            sorter.setRowFilter(RowFilter.regexFilter(searchText));
        } else {
            sorter.setRowFilter(null);
        }
    }


}



