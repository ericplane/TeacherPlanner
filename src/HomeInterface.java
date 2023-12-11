import Data.SchoolClassManager;
import Panels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class HomeInterface {

    JFrame window;
    LoginInterface loginInterface;

    public HomeInterface(JFrame window, LoginInterface loginInterface) {
        this.window = window;
        this.loginInterface = loginInterface;
    }

    public void create() {
        JPanel panel = new JPanel();
        panel.setSize(window.getSize());
        panel.setLayout(new BorderLayout());

        SchoolClassManager schoolClassManager = new SchoolClassManager();

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        JComboBox<String> classDropdown = new JComboBox<>();
        classDropdown.setToolTipText("Selected Class");

        JButton logoutButton = new JButton("Logout");

        JTabbedPane frame = new JTabbedPane();

        Attendance attendance = new Attendance(panel, schoolClassManager);
        Grading grading = new Grading(panel, schoolClassManager);
        Activity activity = new Activity(panel);
        Analytics analytics = new Analytics(panel);
        ManageClasses manageClasses = new ManageClasses(panel, schoolClassManager);

        frame.addTab("Attendance", attendance.create());
        frame.addTab("Grading", grading.create());
        frame.addTab("Activity", activity.create());
        frame.addTab("Analytics", analytics.create());
        frame.addTab("Manage Classes", manageClasses.create());

        DefaultComboBoxModel<String> classModel = new DefaultComboBoxModel<>(schoolClassManager.getSchoolClassNames());
        classDropdown.setModel(classModel);

        if (classDropdown.getSelectedIndex() == 0 && classDropdown.getItemCount() > 0) {
            schoolClassManager.setSelectedClassIndex(0);
        }

        classDropdown.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                int numberOfClasses = classDropdown.getItemCount();
                String[] classNames = schoolClassManager.getSchoolClassNames();

                if (classNames.length != numberOfClasses) {
                    String selectedItem = (String) classDropdown.getSelectedItem();
                    classDropdown.removeAllItems();
                    classDropdown.setModel(new DefaultComboBoxModel<>(classNames));

                    for (int i = 0; i < classDropdown.getItemCount(); i++) {
                        if(Objects.equals(classDropdown.getItemAt(i), selectedItem)) {
                            classDropdown.setSelectedIndex(i);
                            break;
                        }
                    }

                    classDropdown.showPopup();

                    if (classNames.length == 0) updateTabs(frame, false);
                    if (classNames.length == 1) {
                        updateTabs(frame, true);
                        schoolClassManager.setSelectedClassIndex(classDropdown.getSelectedIndex());
                    }
                }
            }
        });

        if (classDropdown.getSelectedItem() == null) {
            updateTabs(frame, false);
            frame.setSelectedIndex(4);
        }

        classDropdown.addItemListener(itemEvent -> {
            String selectedClass = itemEvent.getItem().toString();

            if (selectedClass != null && !selectedClass.isEmpty()) {
                updateTabs(frame, true);
                schoolClassManager.setSelectedClassIndex(classDropdown.getSelectedIndex());
            } else {
                schoolClassManager.setSelectedClassIndex(-1);
                updateTabs(frame, false);
            }
        });

        logoutButton.addActionListener(actionEvent -> loginInterface.create(panel));

        topPanel.add(classDropdown, BorderLayout.CENTER);
        topPanel.add(logoutButton, BorderLayout.LINE_END);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(frame, BorderLayout.CENTER);

        window.add(panel);
        window.setVisible(true);

        loginInterface.create(panel);
    }

    private void updateTabs(JTabbedPane frame, boolean enabled) {
        frame.setEnabledAt(0, enabled);
        frame.setEnabledAt(1, enabled);
        frame.setEnabledAt(2, enabled);
        frame.setEnabledAt(3, enabled);
    }
}
