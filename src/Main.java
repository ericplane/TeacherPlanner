import Data.DataManage;

import javax.swing.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        int windowWidth = 1200;
        int windowHeight = 700;

        JFrame window = new JFrame("Teacher Planner");
        window.setResizable(true);
        window.setSize(windowWidth, windowHeight);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);

        doesHaveData();

        // Login System
        LoginInterface loginInterface = new LoginInterface(window);

        // Home Interface
        HomeInterface homeInterface = new HomeInterface(window, loginInterface);
        homeInterface.create();
    }

    private static void doesHaveData() {
        File file = new File("data.dat");
        if (!file.exists()) {
            new DataManage();
            DataManage.createNewDataFile();
        } else {
            System.out.println("Data file already exists");
        }
    }
}