package Classes;

import java.io.Serializable;

public class SchoolClass implements Serializable {
    public Student[] students;

    public String className;

    public String[] assignments;

    public SchoolClass(String className) {
        this.className = className;
    }
}
