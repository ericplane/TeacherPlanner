package Panels;

import Classes.SchoolClass;
import Classes.Student;
import Data.SchoolClassManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.regex.Pattern;

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
        selectionPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JComboBox<String> assignmentDropdown = new JComboBox<>();

        JButton createAssignment = new JButton("Create Assignment");
        JButton deleteAssignment = new JButton("Delete Selected Assignment");

        JTable gradeTable = new JTable(tableModel);

        TableColumn gradeColumn = gradeTable.getColumnModel().getColumn(1);
        gradeColumn.setCellEditor(new DefaultCellEditor(new JComboBox<Integer>(new Integer[]{0,1,2,3,4,5,6,7})));

        TableColumn commentColumn = gradeTable.getColumnModel().getColumn(2);


        schoolClassManager.addSchoolClassChangeListener(this::updateTable);

        buttonPanel.add(createAssignment);
        buttonPanel.add(deleteAssignment);

        selectionPanel.add(assignmentDropdown, BorderLayout.CENTER);
        selectionPanel.add(buttonPanel, BorderLayout.LINE_END);

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
