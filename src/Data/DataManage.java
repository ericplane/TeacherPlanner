package Data;

import Classes.SchoolClass;

import java.io.*;

public class DataManage {
    public void saveClassData(SchoolClass[] classes) {
        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream("data.dat"))) {
            stream.writeObject(classes);
            System.out.println("Class Data saved successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SchoolClass[] loadClassData() {
        try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream("data.dat"))) {
            SchoolClass[] classes = (SchoolClass[]) stream.readObject();
            System.out.println("Class Data loaded successfully");
            return classes;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void createNewDataFile() {
        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream("data.dat"))) {
            System.out.println("Data file created successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}