package Classes;

import Enums.Gender;
import Enums.Presence;

import java.util.Date;
import java.util.Hashtable;

public class Student {
    public String name;
    public Date dateOfBirth;
    public Gender gender;
    public Integer[] grades;
    public Hashtable<Date, Presence> presence = new Hashtable<>();
    public Hashtable<Date, String> absences = new Hashtable<>();

    public Student(String name, Date dateOfBirth, Gender gender) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }
}