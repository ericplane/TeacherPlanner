package Panels;

import Classes.SchoolClass;
import Classes.Student;
import Data.SchoolClassManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Grading {
    SchoolClassManager schoolClassManager;
    JPanel homePanel;
    String[] columnNames = new String[]{"Name", "Grade", "Comment"};
    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {return column == 1;}
    };

    public Grading(JPanel homePanel, SchoolClassManager schoolClassManager) {
        this.homePanel = homePanel;
        this.schoolClassManager = schoolClassManager;
    }

    public JPanel create() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new FlowLayout());

        JTable gradeTable = new JTable(tableModel);

        schoolClassManager.addSchoolClassChangeListener(this::updateTable);

        panel.add(selectionPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(gradeTable), BorderLayout.CENTER);

        selectionPanel.setVisible(true);
        gradeTable.setVisible(true);

        updateTable();

        return panel;
    }

    private void updateTable() {
        SchoolClass schoolClass = schoolClassManager.getSchoolClass();
        if (schoolClass == null) return;
        Student[] students = schoolClass.students;

        tableModel.setNumRows(0);

        if (students == null) return;

        for (Student student : students) {
            tableModel.addRow(new Object[]{student.name, 1, "No Comment"});
        }
    }
}
