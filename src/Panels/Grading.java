package Panels;

import Data.SchoolClassManager;

import javax.swing.*;

public class Grading {
    SchoolClassManager schoolClassManager;
    JPanel homePanel;

    public Grading(JPanel homePanel, SchoolClassManager schoolClassManager) {
        this.homePanel = homePanel;
        this.schoolClassManager = schoolClassManager;
    }

    public JPanel create() {
        JPanel panel = new JPanel();
        JPanel selectionPanel = new JPanel();

        return panel;
    }
}
