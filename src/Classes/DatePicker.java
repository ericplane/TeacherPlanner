package Classes;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DatePicker extends JPanel {
    private final List<DateChangeListener> dateChangeListeners = new ArrayList<>();
    private final JComboBox<String> dayDropdown;
    private final JComboBox<String> monthDropdown;
    private final JComboBox<String> yearDropdown;

    public DatePicker(boolean isFuture) {
        dayDropdown = new JComboBox<>();
        monthDropdown = new JComboBox<>();
        yearDropdown = new JComboBox<>();

        dayDropdown.setToolTipText("Day");
        monthDropdown.setToolTipText("Month");
        yearDropdown.setToolTipText("Year");

        for (int i = 1; i <= 12; i++) {
            monthDropdown.addItem(String.valueOf(i));
        }

        int currentYear = LocalDate.now().getYear();
        if (!isFuture) {
            for (int i = currentYear; i >= 1900; i--) {
                yearDropdown.addItem(String.valueOf(i));
            }
        } else {
            for (int i = currentYear; i <= currentYear + 2; i++) {
                yearDropdown.addItem(String.valueOf(i));
            }
        }

        monthDropdown.addActionListener(e -> updateDayDropdown(isFuture));

        yearDropdown.addActionListener(e -> updateDayDropdown(isFuture));

        this.add(dayDropdown);
        this.add(monthDropdown);
        this.add(yearDropdown);
        this.setLayout(new FlowLayout());

        LocalDate currentDate = LocalDate.now();
        yearDropdown.setSelectedItem(String.valueOf(currentDate.getYear()));
        monthDropdown.setSelectedItem(String.valueOf(currentDate.getMonthValue()));

        updateDayDropdown(isFuture);

        dayDropdown.setSelectedItem(String.valueOf(currentDate.getDayOfMonth()));

        yearDropdown.addItemListener(e -> notifyDateChangeListeners());
        monthDropdown.addItemListener(e -> notifyDateChangeListeners());
        dayDropdown.addItemListener(e -> notifyDateChangeListeners());
    }

    private void updateDayDropdown(boolean isFuture) {
        LocalDate currentDate = LocalDate.now();
        int selectedMonth = Integer.parseInt((String) Objects.requireNonNull(monthDropdown.getSelectedItem()));
        int currentMonth = currentDate.getMonthValue();
        int selectedYear = Integer.parseInt((String) Objects.requireNonNull(yearDropdown.getSelectedItem()));
        int currentYear = currentDate.getYear();
        int maxDay = YearMonth.of(selectedYear, selectedMonth).lengthOfMonth();

        if (selectedYear == currentYear && selectedMonth == currentMonth && !isFuture) {
            maxDay = currentDate.getDayOfMonth();
        }

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

    public void addDateChangeListener(DateChangeListener listener) {
        dateChangeListeners.add(listener);
    }

    private void notifyDateChangeListeners() {
        String selectedDate = getSelectedDate();
        for (DateChangeListener listener : dateChangeListeners) {
            listener.onDateChange(selectedDate);
        }
    }
}
