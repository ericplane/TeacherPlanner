package Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.Date;
import java.util.Objects;

public class DatePicker extends JPanel {
    private final JComboBox<String> dayDropdown;
    private final JComboBox<String> monthDropdown;
    private final JComboBox<String> yearDropdown;

    public DatePicker() {
        dayDropdown = new JComboBox<>();
        monthDropdown = new JComboBox<>();
        yearDropdown = new JComboBox<>();

        for (int i = 1; i <= 12; i++) {
            monthDropdown.addItem(String.valueOf(i));
        }

        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear; i >= 1900; i--) {
            yearDropdown.addItem(String.valueOf(i));
        }

        monthDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDayDropdown();
            }
        });

        yearDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDayDropdown();
            }
        });

        this.add(dayDropdown);
        this.add(monthDropdown);
        this.add(yearDropdown);
        this.setLayout(new FlowLayout());

        LocalDate currentDate = LocalDate.now();
        yearDropdown.setSelectedItem(String.valueOf(currentDate.getYear()));
        monthDropdown.setSelectedItem(String.valueOf(currentDate.getMonthValue()));

        updateDayDropdown();

        dayDropdown.setSelectedItem(String.valueOf(currentDate.getDayOfMonth()));
    }

    private void updateDayDropdown() {
        int selectedMonth = Integer.parseInt((String) Objects.requireNonNull(monthDropdown.getSelectedItem()));
        int selectedYear = Integer.parseInt((String) Objects.requireNonNull(yearDropdown.getSelectedItem()));
        int maxDay = YearMonth.of(selectedYear, selectedMonth).lengthOfMonth();

        dayDropdown.removeAllItems();
        for (int i = 1; i <= maxDay; i++) {
            dayDropdown.addItem(String.valueOf(i));
        }
    }

    public String getSelectedDate() {
        String day = (String) dayDropdown.getSelectedItem();
        String month = (String) monthDropdown.getSelectedItem();
        String year = (String) yearDropdown.getSelectedItem();

        return day + "/" + month + "/" + year;
    }
}
