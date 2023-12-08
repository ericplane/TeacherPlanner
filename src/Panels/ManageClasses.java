package Panels;

import Classes.SchoolClass;
import Classes.Student;
import Data.DataManage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class ManageClasses {
    JPanel homePanel;
    JTable classTable;
    String[] columnNames = new String[]{"Name", "Gender", "Date of Birth"};
    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
    DataManage dataManage = new DataManage();
    SchoolClass[] classes;
    public String[] classNames = new String[1];
    public int selectedClassIndex = -1;

    public ManageClasses(JPanel homePanel) {
        this.homePanel = homePanel;
        this.classes = dataManage.loadClassData();

        if (this.classes != null) {
            if (this.classes.length > 0) {
                updateClassNames();
            }
        }
    }

    public JPanel create() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton createClass = new JButton("Create Class");
        JButton createStudent = new JButton("Create Student");

        classTable = new JTable(tableModel);
        updateTable();

        buttonPanel.add(createClass);
        buttonPanel.add(createStudent);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(classTable), BorderLayout.CENTER);

        createClass.setVisible(true);
        createStudent.setVisible(true);
        classTable.setVisible(true);
        buttonPanel.setVisible(true);

        createClass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                createClass();
            }
        });

        createStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                createStudent();
                updateTable();
            }
        });

        return panel;
    }

    private void createClass() {
        String className = JOptionPane.showInputDialog("Please enter the class name");

        if (className == null) return;

        if (!className.isEmpty() && className.split(" ").length == 1) {

            if (doesAlreadyHaveClass(className)) {
                JOptionPane.showMessageDialog(null, "This class already exists.", "Already Exists", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(null, "Successfully created the class " + className + ".", "Class Created", JOptionPane.INFORMATION_MESSAGE);

            if (classes == null) {
                classes = new SchoolClass[]{new SchoolClass(className)};
            } else {
                classes = Arrays.copyOf(classes, classes.length+1);
                classes[classes.length-1] = new SchoolClass(className);
            }
            updateClassNames();
            dataManage.saveClassData(classes);
        } else {
            JOptionPane.showMessageDialog(null, "Please enter a valid class name. It must include no spaces and can't be empty.", "Invalid Class Name", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createStudent() {
        if (selectedClassIndex < 0) return;
        SchoolClass schoolClass = classes[selectedClassIndex];
        Student[] students = schoolClass.students;


    }

    public void updateTable() {
        if (selectedClassIndex < 0) return;
        SchoolClass schoolClass = classes[selectedClassIndex];
        Student[] students = schoolClass.students;

        tableModel.setNumRows(0);

        for (Student student : students) {
            tableModel.addRow(new String[]{student.name, student.gender.toString(), String.valueOf(student.dateOfBirth)});
        }
    }

    private void updateClassNames() {
        String[] tempClassNames = new String[classes.length];
        int i = 0;
        for (SchoolClass schoolClass : classes) {
            tempClassNames[i] = schoolClass.className;
            i++;
        }
        classNames = tempClassNames;
    }

    private boolean doesAlreadyHaveClass(String className) {
        for (String name : classNames) {
            if (className.equals(name)) {
                return true;
            }
        }

        return false;
    }
}
