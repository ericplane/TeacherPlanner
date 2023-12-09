package Panels;

import Classes.DatePicker;
import Classes.SchoolClass;
import Classes.Student;
import Data.SchoolClassManager;
import Enums.Presence;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.EventObject;

public class Attendance {
    JPanel homePanel;
    SchoolClassManager schoolClassManager;
    DatePicker datePicker;
    String[] columnNames = new String[]{"Name", "Attendance", "Reason"};
    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column != 0;
        }
    };

    public Attendance(JPanel homePanel, SchoolClassManager schoolClassManager) {
        this.homePanel = homePanel;
        this.schoolClassManager = schoolClassManager;
    }

    public JPanel create() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        datePicker = new DatePicker();
        JTable attendanceTable = new JTable(tableModel);

        TableColumn presenceColumn = attendanceTable.getColumnModel().getColumn(1);
        presenceColumn.setCellEditor(new DefaultCellEditor(new JComboBox<>(Presence.values())));

        TableColumn reasonColumn = attendanceTable.getColumnModel().getColumn(2);
        reasonColumn.setCellEditor(new DefaultCellEditor(new JTextField()) {
            @Override
            public boolean isCellEditable(EventObject anEvent) {
                int rowIndex = attendanceTable.getSelectedRow();
                if (rowIndex < 0) return false;
                Object valueInSecondColumn = attendanceTable.getValueAt(rowIndex, 1);
                if (valueInSecondColumn == null) return false;
                return valueInSecondColumn.equals(Presence.ABSENT);
            }
        });

        tableModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column != 1 && column != 2) return;
            Object data = tableModel.getValueAt(row, column);
            String date = datePicker.getSelectedDate();

            SchoolClass schoolClass = schoolClassManager.getSchoolClass();
            if (schoolClass == null) return;

            if (column == 1) {
                // Presence Update
                schoolClass.students[row].presence.put(date, (Presence) data);

                if (data != Presence.ABSENT) {
                    schoolClass.students[row].absences.remove(date);
                }
            } else {
                // Reason Update
                schoolClass.students[row].absences.put(date, (String) data);
            }
            schoolClassManager.setSchoolClass(schoolClass);
        });

        datePicker.addDateChangeListener(newDate -> updateTable());

        schoolClassManager.addSchoolClassChangeListener(this::updateTable);

        panel.add(datePicker, BorderLayout.NORTH);
        panel.add(new JScrollPane(attendanceTable), BorderLayout.CENTER);

        datePicker.setVisible(true);
        attendanceTable.setVisible(true);

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
            tableModel.addRow(new Object[]{student.name, student.presence.getOrDefault(datePicker.getSelectedDate(), null), student.absences.getOrDefault(datePicker.getSelectedDate(), "N/A")});
        }
    }
}
