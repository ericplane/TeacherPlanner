import Data.DataManage;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        int windowWidth = 1200;
        int windowHeight = 700;

        JFrame window = new JFrame("Teacher Planner");
        window.setResizable(false);
        window.setBounds(screenWidth/2 - windowWidth/2, screenHeight/2 - windowHeight/2, windowWidth, windowHeight);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        doesHaveData();

        // Login System
        LoginInterface loginInterface = new LoginInterface(window);
        loginInterface.create();

        // Home Interface
        HomeInterface homeInterface = new HomeInterface(window);
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