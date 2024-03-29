package Classes;

import Enums.Gender;
import Enums.Presence;

import java.io.Serializable;
import java.util.Hashtable;

public class Student implements Serializable {
    public String name;
    public String dateOfBirth;
    public Gender gender;
    public Hashtable<String, Integer> grades = new Hashtable<>();
    public Hashtable<String, String> gradesComments = new Hashtable<>();
    public Hashtable<String, Presence> presence = new Hashtable<>();
    public Hashtable<String, String> absences = new Hashtable<>();

    public Student(String name, String dateOfBirth, Gender gender) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }
}