package Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Hashtable;

public class PieChart extends JPanel {
    Hashtable<String, Integer> data;

    public PieChart(Hashtable<String, Integer> data) {
        this.data = data;
        this.setLayout(new BorderLayout()); // Set the layout manager
        this.setSize(400, 400);
        this.setPreferredSize(new Dimension(400,400));

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                String section = getSectionAt(x, y);
                if (section != null) {
                    int value = data.get(section);
                    double percentage = (double) value / 360 * 100;
                    String tooltip = section + ": " + String.format("%.2f", percentage) + "%";
                    setToolTipText(tooltip);
                } else {
                    setToolTipText(null);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        int chartDiameter = Math.min(width, height) - 100;
        int x = (width - chartDiameter) / 2;
        int y = (height - chartDiameter) / 2;
        int startAngle = 0;

        for (String category : data.keySet()) {
            int value = data.get(category);
            g.setColor(getRandomColor());
            g.fillArc(x, y, chartDiameter, chartDiameter, startAngle, value);
            startAngle += value;
        }
    }

    private int getTotalValue() {
        int total = 0;
        for (int value : data.values()) {
            total += value;
        }
        return total;
    }

    private Color getRandomColor() {
        return new Color((int) (Math.random() * 0x1000000));
    }

    private String getSectionAt(int x, int y) {
        double angle = getAngle(x, y);

        // Calculate the section based on the angle
        double total = getTotalValue();
        double currentAngle = 0;
        for (String category : data.keySet()) {
            int value = data.get(category);
            double arcAngle = (360.0 * value / total) * (Math.PI / 180); // Convert degrees to radians
            if (angle >= currentAngle && angle < currentAngle + arcAngle) {
                return category;
            }
            currentAngle += arcAngle;
        }

        return null; // No section found at the specified angle
    }

    private double getAngle(int x, int y) {
        int width = getWidth();
        int height = getHeight();
        int chartDiameter = Math.min(width, height) - 100;
        int centerX = width / 2;
        int centerY = height / 2;

        // Adjust x and y relative to the center of the pie chart
        int adjustedX = x - centerX;
        int adjustedY = centerY - y; // Reverse the y-axis

        // Calculate the angle in radians
        double angle = Math.atan2(adjustedY, adjustedX);
        angle = (angle < 0) ? (2 * Math.PI + angle) : angle; // Convert negative angles to positive
        return angle;
    }
}
