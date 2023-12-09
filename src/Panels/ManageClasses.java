package Panels;

import Classes.DatePicker;
import Classes.SchoolClass;
import Classes.Student;
import Data.SchoolClassManager;
import Enums.Gender;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class ManageClasses {
    SchoolClassManager schoolClassManager;
    JPanel homePanel;
    JTable classTable;
    String[] columnNames = new String[]{"Name", "Gender", "Date of Birth (DD/MM/YYYY)"};
    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public ManageClasses(JPanel homePanel, SchoolClassManager schoolClassManager) {
        this.homePanel = homePanel;
        this.schoolClassManager = schoolClassManager;
    }

    public JPanel create() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton createClass = new JButton("Create Class");
        JButton createStudent = new JButton("Create Student");
        JButton deleteClass = new JButton("Delete Selected Class");

        classTable = new JTable(tableModel);
        updateTable();

        buttonPanel.add(createClass);
        buttonPanel.add(createStudent);
        buttonPanel.add(deleteClass);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(classTable), BorderLayout.CENTER);

        createClass.setVisible(true);
        createStudent.setVisible(true);
        classTable.setVisible(true);
        buttonPanel.setVisible(true);

        createClass.addActionListener(actionEvent -> createClass());

        createStudent.addActionListener(actionEvent -> createStudentPopup());

        deleteClass.addActionListener(e -> {
            SchoolClass selectedSchoolClass = schoolClassManager.getSchoolClass();
            if (selectedSchoolClass == null) return;
            SchoolClass[] schoolClasses = schoolClassManager.getSchoolClasses();
            int amountOfSchoolClasses = schoolClasses.length;

            if (amountOfSchoolClasses - 1 == 0) {
                schoolClassManager.setSchoolClasses(null);
                return;
            }

            SchoolClass[] tempClasses = new SchoolClass[amountOfSchoolClasses - 1];

            int classIndex = 0;

            for (SchoolClass schoolClass : schoolClasses) {
                if (selectedSchoolClass == schoolClass) continue;
                tempClasses[classIndex] = schoolClass;
                classIndex++;
            }

            schoolClassManager.setSchoolClasses(tempClasses);
        });

        classTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    int selectedRow = classTable.getSelectedRow();
                    if (selectedRow != -1) {
                        tableModel.removeRow(selectedRow);
                        SchoolClass selectedSchoolClass = schoolClassManager.getSchoolClass();
                        int amountOfStudents = selectedSchoolClass.students.length;

                        if (amountOfStudents - 1 == 0) {
                            selectedSchoolClass.students = null;
                            schoolClassManager.setSchoolClass(selectedSchoolClass);
                            return;
                        }
                        Student[] students = new Student[amountOfStudents - 1];

                        int studentIndex = 0;

                        for (Student student : selectedSchoolClass.students) {
                            if (selectedSchoolClass.students[selectedRow] == student) continue;
                            students[studentIndex] = student;
                            studentIndex++;
                        }

                        selectedSchoolClass.students = students;
                        schoolClassManager.setSchoolClass(selectedSchoolClass);
                    }
                }
            }
        });

        schoolClassManager.addSchoolClassChangeListener(this::updateTable);

        return panel;
    }

    private void createClass() {
        String className = JOptionPane.showInputDialog("Please enter the class name");
        SchoolClass[] schoolClasses = schoolClassManager.getSchoolClasses();

        if (className == null) return;

        if (!className.isEmpty() && className.split(" ").length == 1) {

            if (doesAlreadyHaveClass(className)) {
                JOptionPane.showMessageDialog(null, "This class already exists.", "Already Exists", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(null, "Successfully created the class " + className + ".", "Class Created", JOptionPane.INFORMATION_MESSAGE);

            if (schoolClasses == null) {
                schoolClasses = new SchoolClass[]{new SchoolClass(className)};
            } else {
                schoolClasses = Arrays.copyOf(schoolClasses, schoolClasses.length + 1);
                schoolClasses[schoolClasses.length - 1] = new SchoolClass(className);
            }

            schoolClassManager.setSchoolClasses(schoolClasses);
        } else {
            JOptionPane.showMessageDialog(null, "Please enter a valid class name. It must include no spaces and can't be empty.", "Invalid Class Name", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createStudentPopup() {
        if (schoolClassManager.getSchoolClass() == null) return;

        JDialog popup = new JDialog(null, "Create Student", Dialog.ModalityType.APPLICATION_MODAL);

        JPanel panel = new JPanel();
        // Add Create Student components to the panel
        JLabel nameLabel = new JLabel("Name: ");
        JTextField nameField = new JTextField(16);
        JLabel genderLabel = new JLabel("Gender: ");
        JComboBox<Gender> genderField = new JComboBox<>(Gender.values());
        JLabel dateLabel = new JLabel("Date of Birth: ");
        DatePicker datePicker = new DatePicker();
        JButton createButton = new JButton("Add Student");

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(genderLabel);
        panel.add(genderField);
        panel.add(dateLabel);
        panel.add(datePicker);
        panel.add(createButton);

        popup.add(panel);

        createButton.addActionListener(e -> {
            String studentName = nameField.getText();
            Gender studentGender = (Gender) genderField.getSelectedItem();
            String studentDateOfBirth = datePicker.getSelectedDate();

            if (studentName.isEmpty() || studentName.split(" ").length < 2) {
                JOptionPane.showMessageDialog(null, "Invalid Student Name", "Invalid Student", JOptionPane.ERROR_MESSAGE);
                return;
            }

            createStudent(studentName, studentGender, studentDateOfBirth);
            popup.dispose();
        });

        popup.setSize(220,220);
        popup.setLocationRelativeTo(null);
        popup.setResizable(false);

        popup.setVisible(true);
    }

    private void createStudent(String studentName, Gender studentGender, String studentDateOfBirth) {
        SchoolClass selectedSchoolClass = schoolClassManager.getSchoolClass();
        Student[] students = selectedSchoolClass.students;

        if (students == null) {
            students = new Student[1];
        } else {
            students = Arrays.copyOf(students, students.length + 1);
        }

        students[students.length - 1] = new Student(studentName, studentDateOfBirth, studentGender);

        selectedSchoolClass.students = students;

        schoolClassManager.setSchoolClass(selectedSchoolClass);
    }

    private void updateTable() {
        SchoolClass selectedSchoolClass = schoolClassManager.getSchoolClass();
        if (selectedSchoolClass == null) return;
        Student[] students = selectedSchoolClass.students;

        tableModel.setNumRows(0);

        if (students == null) return;

        for (Student student : students) {
            tableModel.addRow(new String[]{student.name, student.gender.toString(), String.valueOf(student.dateOfBirth)});
        }
    }

    private boolean doesAlreadyHaveClass(String className) {
        for (String name : schoolClassManager.getSchoolClassNames()) {
            if (className.equals(name)) {
                return true;
            }
        }

        return false;
    }
}
