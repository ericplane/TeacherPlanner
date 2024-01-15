package Data;

import Classes.SchoolClass;

import java.io.*;
import java.net.URISyntaxException;

public class DataManage {
    public void saveClassData(SchoolClass[] classes) {
        try {
            String jarPath = new File(DataManage.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            File dataFile = new File(new File(jarPath).getParent(), "data.dat");
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(dataFile));
            stream.writeObject(classes);
            System.out.println("Class Data saved successfully");
            stream.close();
        } catch (IOException | URISyntaxException ignored) {}
    }

    public SchoolClass[] loadClassData() {
        try {
            String jarPath = new File(DataManage.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            File dataFile = new File(new File(jarPath).getParent(), "data.dat");
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(dataFile));
            SchoolClass[] classes = (SchoolClass[]) stream.readObject();
            System.out.println("Class Data loaded successfully");
            stream.close();
            return classes;
        } catch (IOException | ClassNotFoundException | URISyntaxException e) {
            return null;
        }
    }

    public static void createNewDataFile() {
        try {
            String jarPath = new File(DataManage.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            File dataFile = new File(new File(jarPath).getParent(), "data.dat");
            ObjectOutputStream ignored = new ObjectOutputStream(new FileOutputStream(dataFile));
            System.out.println("Data file created successfully");
            ignored.close();
        } catch (IOException | URISyntaxException ignored) {}
    }
}