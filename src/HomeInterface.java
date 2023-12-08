import Panels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomeInterface {

    JFrame window;

    public HomeInterface(JFrame window) {
        this.window = window;
    }

    public void create() {
        JPanel panel = new JPanel();
        panel.setSize(window.getSize());
        panel.setLayout(new BorderLayout());

        ManageClasses manageClasses = new ManageClasses(panel);

        JComboBox<String> classDropdown = new JComboBox<String>();
        classDropdown.setPrototypeDisplayValue("Select Class");

        JTabbedPane frame = new JTabbedPane();

        Attendance attendance = new Attendance(panel);
        Grading grading = new Grading(panel);
        Activity activity = new Activity(panel);
        Analytics analytics = new Analytics(panel);

        frame.addTab("Attendance", attendance.create());
        frame.addTab("Grading", grading.create());
        frame.addTab("Activity", activity.create());
        frame.addTab("Analytics", analytics.create());
        frame.addTab("Manage Classes", manageClasses.create());

        DefaultComboBoxModel<String> classModel = new DefaultComboBoxModel<>(manageClasses.classNames);
        classDropdown.setModel(classModel);

        classDropdown.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                int numberOfClasses = classDropdown.getItemCount();

                if (manageClasses.classNames.length != numberOfClasses) {
                    String selectedItem = classDropdown.getSelectedItem().toString();
                    classDropdown.setModel(new DefaultComboBoxModel<>(manageClasses.classNames));

                    for (int i = 0; i < classDropdown.getItemCount(); i++) {
                        if(classDropdown.getItemAt(i).toString() == selectedItem) {
                            classDropdown.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void focusLost(FocusEvent focusEvent) {

            }
        });

        if (classDropdown.getSelectedItem() == null) {
            updateTabs(frame, false);
            frame.setSelectedIndex(4);
        }

        classDropdown.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                String selectedClass = itemEvent.getItem().toString();

                if (selectedClass != null && !selectedClass.equals("")) {
                    updateTabs(frame, true);
                    manageClasses.selectedClassIndex = classDropdown.getSelectedIndex();
                    manageClasses.updateTable();
                } else {
                    manageClasses.selectedClassIndex = -1;
                }
            }
        });

        panel.add(classDropdown, BorderLayout.NORTH);
        panel.add(frame, BorderLayout.CENTER);

        panel.setVisible(true);

        window.add(panel);
        window.setVisible(true);
    }

    private void updateTabs(JTabbedPane frame, boolean enabled) {
        frame.setEnabledAt(0, enabled);
        frame.setEnabledAt(1, enabled);
        frame.setEnabledAt(2, enabled);
        frame.setEnabledAt(3, enabled);
    }
}
