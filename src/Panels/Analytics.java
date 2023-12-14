package Panels;

import Classes.PieChart;

import javax.swing.*;
import java.util.Hashtable;

public class Analytics {
    JPanel homePanel;

    public Analytics(JPanel homePanel) {
        this.homePanel = homePanel;
    }

    public JPanel create() {
        JPanel panel = new JPanel();

        Hashtable<String, Integer> hashtable = new Hashtable<>();
        hashtable.put("Present", 220);
        hashtable.put("Absent", 30);
        hashtable.put("Late", 110);

        PieChart pieChart = new PieChart(hashtable);
        panel.add(pieChart);
        pieChart.repaint();

        return panel;
    }
}
