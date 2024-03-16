package ui;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import model.*;
import java.util.HashMap;

public class NotesUI extends JPanel {
    private JFrame frame;
    private JTable notesTable;
    private JTableHeader notesTableheader;
    private User user;

    public NotesUI(User u) {

        this.user = u;
        initComponents();

    }

    private void initComponents() {
        HashMap<Integer, String> userNotes = user.getHub().getNote().getListOfNotes();
        notesTable = new JTable(userNotes.size(), 2);
        notesTable.setBounds(10, 11, 384, 536);
    }


}
