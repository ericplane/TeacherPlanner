package Classes;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class LineChart extends JPanel {
    private TreeMap<Date, Integer>data;

    public LineChart(TreeMap<Date, Integer> data) {
        this.data = data;
        this.setPreferredSize(new Dimension(400, 400));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        int xPadding = 50;
        int yPadding = 50;
        int chartWidth = width - 2 * xPadding;
        int chartHeight = height - 2 * yPadding;

        g.drawLine(xPadding, height - yPadding, width - xPadding, height - yPadding); // x axis
        g.drawLine(xPadding, yPadding, xPadding, height - yPadding); // y axis

        g.drawString("Assessments", width / 2, height - 5);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawString("Grades", xPadding - 40, height / 2);

        for (int i = 0; i <= 7; i++) {
            int yPos = height - yPadding - (i * (chartHeight / 7));
            g.drawLine(xPadding - 5, yPos, xPadding, yPos);
            g.drawString(String.valueOf(i), xPadding - 30, yPos + 5);
        }

        double meanX = 3.5;
        double meanY = data.values().stream().mapToDouble(Integer::doubleValue).average().orElse(0);
        double m = calculateSlope(meanX, meanY, data);
        double b = meanY - m * meanX;
        int x1 = xPadding;
        int y1 = (int) (height - yPadding - (m * 0 + b) * (chartHeight / 7));
        int x2 = width - xPadding;
        int y2 = (int) (height - yPadding - (m * 7 + b) * (chartHeight / 7));
        g.setColor(Color.RED);
        g.drawLine(x1, y1, x2, y2);

        g.setColor(Color.BLUE);
        Date prevDate = null;
        int prevValue = 0;
        for (Map.Entry<Date, Integer> entry : data.entrySet()) {
            Date currentDate = entry.getKey();
            int currentValue = entry.getValue();
            if (prevDate != null) {
                long timeRange = data.lastKey().getTime() - data.firstKey().getTime();
                double x1Pixel = xPadding + ((double) (prevDate.getTime() - data.firstKey().getTime()) / timeRange) * chartWidth;
                double y1Pixel = height - yPadding - ((double) prevValue / 7 * chartHeight);
                double x2Pixel = xPadding + ((double) (currentDate.getTime() - data.firstKey().getTime()) / timeRange) * chartWidth;
                double y2Pixel = height - yPadding - ((double) currentValue / 7 * chartHeight);
                g.drawLine((int) x1Pixel, (int) y1Pixel, (int) x2Pixel, (int) y2Pixel);
            }
            prevDate = currentDate;
            prevValue = currentValue;
        }
    }

    public void updateData(TreeMap<Date, Integer> newData) {
        this.data = newData;
        repaint();
    }

    private double calculateSlope(double meanX, double meanY, TreeMap<Date, Integer> data) {
        double numerator = 0;
        double denominator = 0;
        for (Map.Entry<Date, Integer> entry : data.entrySet()) {
            numerator += (entry.getKey().getTime() - meanX) * (entry.getValue() - meanY);
            denominator += Math.pow(entry.getKey().getTime() - meanX, 2);
        }
        return numerator / denominator;
    }
}