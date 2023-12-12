package Panels;

import Classes.DatePicker;
import Classes.SchoolClass;
import Classes.Student;
import Data.SchoolClassChangeListener;
import Data.SchoolClassManager;
import Enums.Gender;
import Enums.Presence;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

public class Grading {
    SchoolClassManager schoolClassManager;
    JPanel homePanel;
    JComboBox<String> assignmentDropdown;
    String[] columnNames = new String[]{"Name", "Grade", "Comment"};
    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {return column != 0;}
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

        assignmentDropdown = new JComboBox<>();

        JButton createAssignment = new JButton("Create Assignment");
        JButton deleteAssignment = new JButton("Delete Selected Assignment");

        JTable gradeTable = new JTable(tableModel);

        TableColumn gradeColumn = gradeTable.getColumnModel().getColumn(1);
        gradeColumn.setCellEditor(new DefaultCellEditor(new JComboBox<Integer>(new Integer[]{0,1,2,3,4,5,6,7})));

        TableColumn commentColumn = gradeTable.getColumnModel().getColumn(2);


        schoolClassManager.addSchoolClassChangeListener(() -> {
            updateDropdown();
            updateTable();
        });

        createAssignment.addActionListener(actionEvent -> createTaskPopup());

        deleteAssignment.addActionListener(actionEvent -> {
            SchoolClass schoolClass = schoolClassManager.getSchoolClass();
            if (schoolClass == null) return;
            if (schoolClass.assignments == null) return;

            String selectedAssignment = (String) assignmentDropdown.getSelectedItem();
            int amountOfAssignments = schoolClass.assignments.length;

            for (Student student : schoolClass.students) {
                student.grades.remove(selectedAssignment);
                student.gradesComments.remove(selectedAssignment);
            }

            if (amountOfAssignments - 1 == 0) {
                schoolClass.assignments = null;
                schoolClassManager.setSchoolClass(schoolClass);
                return;
            }

            String[] tempAssignments = new String[amountOfAssignments - 1];

            int assignmentIndex = 0;

            for (String assignment : schoolClass.assignments) {
                if (Objects.equals(selectedAssignment, assignment)) continue;
                tempAssignments[assignmentIndex] = assignment;
                assignmentIndex++;
            }

            schoolClass.assignments = tempAssignments;
            schoolClassManager.setSchoolClass(schoolClass);
        });

        assignmentDropdown.addItemListener(listener -> updateTable());

        tableModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column != 1 && column != 2) return;
            Object data = tableModel.getValueAt(row, column);
            String assignment = (String) assignmentDropdown.getSelectedItem();

            SchoolClass schoolClass = schoolClassManager.getSchoolClass();
            if (schoolClass == null) return;

            if (column == 1) {
                // Grade Update
                schoolClass.students[row].grades.put(assignment, (int) data);
            } else {
                // Comment Update
                schoolClass.students[row].gradesComments.put(assignment, (String) data);
            }
            schoolClassManager.setSchoolClass(schoolClass);
        });

        buttonPanel.add(createAssignment);
        buttonPanel.add(deleteAssignment);

        selectionPanel.add(assignmentDropdown, BorderLayout.CENTER);
        selectionPanel.add(buttonPanel, BorderLayout.LINE_END);

        panel.add(selectionPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(gradeTable), BorderLayout.CENTER);

        selectionPanel.setVisible(true);
        gradeTable.setVisible(true);

        updateTable();
        updateDropdown();

        return panel;
    }

    private void createTaskPopup() {
        if (schoolClassManager.getSchoolClass() == null) return;

        JDialog popup = new JDialog(null, "Create Assignment", Dialog.ModalityType.APPLICATION_MODAL);

        JPanel panel = new JPanel();
        // Add Create Assignment components to the panel
        JLabel nameLabel = new JLabel("Assignment Name: ");
        JTextField nameField = new JTextField(16);
        JLabel dateLabel = new JLabel("Due Date: ");
        DatePicker datePicker = new DatePicker(true);
        JButton createButton = new JButton("Create Assignment");

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(dateLabel);
        panel.add(datePicker);
        panel.add(createButton);

        popup.add(panel);

        createButton.addActionListener(e -> {
            String taskName = nameField.getText();
            String taskDueDate = datePicker.getSelectedDate();

            if (taskName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Invalid Assignment Name", "Invalid Assignment", JOptionPane.ERROR_MESSAGE);
                return;
            }

            createTask(taskName, taskDueDate);
            popup.dispose();
        });

        popup.setSize(220,180);
        popup.setLocationRelativeTo(null);
        popup.setResizable(false);

        popup.setVisible(true);
    }

    private void createTask(String taskName, String dueDate) {
        SchoolClass selectedSchoolClass = schoolClassManager.getSchoolClass();
        String[] assignments = selectedSchoolClass.assignments;

        if (assignments == null) {
            assignments = new String[1];
        } else {
            assignments = Arrays.copyOf(assignments, assignments.length + 1);
        }

        assignments[assignments.length - 1] = taskName + " (" + dueDate + ")";
        selectedSchoolClass.assignments = assignments;

        schoolClassManager.setSchoolClass(selectedSchoolClass);

        updateDropdown();
    }

    private void updateTable() {
        SchoolClass schoolClass = schoolClassManager.getSchoolClass();
        if (schoolClass == null) return;
        Student[] students = schoolClass.students;

        String selectedItem = (String) assignmentDropdown.getSelectedItem();

        tableModel.setNumRows(0);

        if (students == null) return;

        if (selectedItem == null) return;

        for (Student student : students) {
            tableModel.addRow(new Object[]{student.name, student.grades.getOrDefault(selectedItem, 0), student.gradesComments.getOrDefault(selectedItem, "No Comment")});
        }
    }

    private void updateDropdown() {
        SchoolClass schoolClass = schoolClassManager.getSchoolClass();
        if (schoolClass == null) return;
        String selectedItem = (String) assignmentDropdown.getSelectedItem();

        assignmentDropdown.removeAllItems();

        if (schoolClass.assignments == null) {
            assignmentDropdown.setModel(new DefaultComboBoxModel<>(new String[0]));
        } else {
            assignmentDropdown.setModel(new DefaultComboBoxModel<>(schoolClass.assignments));
        }

        if (selectedItem == null) {
            updateTable();
            return;
        }

        for (int i = 0; i < assignmentDropdown.getItemCount(); i++) {
            if(Objects.equals(assignmentDropdown.getItemAt(i), selectedItem)) {
                assignmentDropdown.setSelectedIndex(i);
                break;
            }
        }

        updateTable();
    }
}
