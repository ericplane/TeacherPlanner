package Classes;

import Enums.Gender;
import Enums.Presence;

import java.io.Serializable;
import java.util.Date;
import java.util.Hashtable;

public class Student implements Serializable {
    public String name;
    public String dateOfBirth;
    public Gender gender;
    public Integer[] grades;
    public Hashtable<Date, Presence> presence = new Hashtable<>();
    public Hashtable<Date, String> absences = new Hashtable<>();

    public Student(String name, String dateOfBirth, Gender gender) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }
}