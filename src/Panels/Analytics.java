package Panels;

import Classes.LineChart;
import Classes.PieChart;
import Classes.Student;
import Data.SchoolClassManager;
import Enums.Presence;
import com.sun.source.tree.Tree;

import javax.sound.sampled.Line;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class Analytics {
    JPanel homePanel;
    SchoolClassManager schoolClassManager;

    public Analytics(JPanel homePanel, SchoolClassManager schoolClassManager) {
        this.homePanel = homePanel;
        this.schoolClassManager = schoolClassManager;
    }

    public JPanel create() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JComboBox<String> studentSelector = new JComboBox<>();

        topPanel.add(studentSelector);

        fillComboBox(studentSelector);

        Hashtable<String, Integer> hashtable = new Hashtable<>();
        hashtable.put("Present", 360);
        hashtable.put("Late", 0);
        hashtable.put("Absent", 0);

        TreeMap<Date, Integer> lineChartSampleData = new TreeMap<>();
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            lineChartSampleData.put(dateFormat.parse("01/01/2024"), 5);
            lineChartSampleData.put(dateFormat.parse("01/02/2024"), 6);
            lineChartSampleData.put(dateFormat.parse("01/03/2024"), 4);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(1, 2));

        JPanel pieChartPanel = new JPanel();
        pieChartPanel.setLayout(new BorderLayout());
        JLabel pieChartLabel = new JLabel("Attendance");
        pieChartPanel.add(pieChartLabel, BorderLayout.NORTH);

        PieChart pieChart = new PieChart(hashtable);
        pieChartPanel.add(pieChart);

        JPanel lineChartPanel = new JPanel();
        lineChartPanel.setLayout(new BorderLayout());
        JLabel lineChartLabel = new JLabel("Grades");
        lineChartPanel.add(lineChartLabel, BorderLayout.NORTH);

        LineChart lineChart = new LineChart(lineChartSampleData);
        lineChartPanel.add(lineChart);

        centerPanel.add(pieChartPanel);
        centerPanel.add(lineChartPanel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(2,1));
        JLabel predicatedGradesLabel = new JLabel("Predicted Grades: ");
        JLabel totalAttendanceLabel = new JLabel("Total Attendance: ");
        bottomPanel.add(predicatedGradesLabel);
        bottomPanel.add(totalAttendanceLabel);

        updateCharts("Whole Class", pieChart, totalAttendanceLabel, lineChart, predicatedGradesLabel);

        schoolClassManager.addSchoolClassChangeListener(() -> {
            fillComboBox(studentSelector);
            updateCharts("Whole Class", pieChart, totalAttendanceLabel, lineChart, predicatedGradesLabel);
        });

        studentSelector.addItemListener(itemEvent -> {
            if (itemEvent.getStateChange() == ItemEvent.DESELECTED) return;
            String selectedStudent = itemEvent.getItem().toString();

            updateCharts(selectedStudent, pieChart, totalAttendanceLabel, lineChart, predicatedGradesLabel);
        });

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        pieChart.repaint();
        lineChart.repaint();

        return panel;
    }

    private Collection<? extends String> getStudentNames() {
        if (schoolClassManager.getSchoolClass() == null) return null;
        if (schoolClassManager.getSchoolClass().students == null) return null;
        return List.of(Arrays.stream(schoolClassManager.getSchoolClass().students).map(Student::getName).toArray(String[]::new));
    }

    private void fillComboBox(JComboBox<String> comboBox) {
        if (schoolClassManager.getSchoolClass() == null) return;
        if (schoolClassManager.getSchoolClass().students == null) return;
        comboBox.removeAllItems();
        DefaultComboBoxModel<String> defaultComboBoxModel = new DefaultComboBoxModel<>();
        defaultComboBoxModel.insertElementAt("Whole Class", 0);
        defaultComboBoxModel.addAll(getStudentNames());
        defaultComboBoxModel.setSelectedItem("Whole Class");
        comboBox.setModel(defaultComboBoxModel);
    }

    private void updateCharts(String selectedStudent, PieChart pieChart, JLabel totalAttendanceLabel, LineChart lineChart, JLabel predicatedGradesLabel) {
        if (!selectedStudent.equals("Whole Class")) {
            String[] students = Objects.requireNonNull(getStudentNames()).toArray(new String[0]);
            Map<String, Integer> studentIndexMap = new HashMap<>();
            for (int i = 0; i < students.length; i++) {
                studentIndexMap.put(students[i], i);
            }

            int index = studentIndexMap.get(selectedStudent);

            updatePieChart(index, pieChart, totalAttendanceLabel);
            updateLineChart(index, lineChart, predicatedGradesLabel);
        }

        updatePieChart(999, pieChart, totalAttendanceLabel);
        updateLineChart(999, lineChart, predicatedGradesLabel);
    }

    private void updatePieChart(int studentIndex, PieChart pieChart, JLabel totalAttendanceLabel) {
        if (studentIndex == 999) {
         return;
        }

        Student student = schoolClassManager.getSchoolClass().students[studentIndex];
        Hashtable<String, Presence> presenceData = student.presence;

        int presentCount = 0;
        int lateCount = 0;
        int absentCount = 0;

        for (Presence presence : presenceData.values()) {
            switch (presence) {
                case PRESENT:
                    presentCount++;
                    break;
                case LATE:
                    lateCount++;
                    break;
                case ABSENT:
                    absentCount++;
                    break;
            }
        }

        double total = presentCount + lateCount + absentCount;

        if (total == 0) return;

        double presentPercentage = (presentCount / total) * 360;
        double latePercentage = (lateCount / total) * 360;
        double absentPercentage = (absentCount / total) * 360;

        Hashtable<String, Integer> dataTable = new Hashtable<>();
        dataTable.put("Present", (int) presentPercentage);
        dataTable.put("Late", (int) latePercentage);
        dataTable.put("Absent", (int) absentPercentage);
        pieChart.updateData(dataTable);

        double totalAttendance = (presentCount + lateCount) / total;
        totalAttendanceLabel.setText("Total Attendance: " + (int) (totalAttendance * 100) + "%");
    }

    private void updateLineChart(int studentIndex, LineChart lineChart, JLabel predicatedGradesLabel) {
        if (studentIndex == 999) {
            return;
        }

        Student student = schoolClassManager.getSchoolClass().students[studentIndex];
        Hashtable<String, Integer> grades = student.grades;

        TreeMap<Date, Integer> gradesData = new TreeMap<>();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (String assignment : grades.keySet()) {
            try {
                String dateString = assignment.substring(assignment.indexOf("(") + 1, assignment.indexOf(")"));
                Date date = dateFormat.parse(dateString);

                if (date != null) {
                    gradesData.put(date, grades.get(assignment));
                } else {
                    System.out.println("Error: Failed to parse date for assignment: " + assignment);
                }
            } catch (ParseException e) {
                System.out.println("Error: Failed to parse date for assignment: " + assignment);
                e.printStackTrace();
            }
        }

        lineChart.updateData(gradesData);

        int totalGrades = grades.values().stream().mapToInt(Integer::intValue).sum();
        double predictedGrades = (totalGrades / (grades.size() * 7.0));
        predicatedGradesLabel.setText("Predicted Grades: " + (int) (predictedGrades * 7));
    }
}
