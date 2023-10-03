package src.classes;

import java.util.ArrayList;
import java.util.List;
import java.io.*;
public class Faculty implements  Serializable {
    private String name;
    private String abbreviation;
    private List<Student> students = new ArrayList<Student>();
    private StudyField studyField;
    private static final long serialVersionUID = 1L;


    public Faculty(String name, String abbreviation, List<Student> students, StudyField studyField){
        this.name=name;
        this.abbreviation=abbreviation;
        this.students=students;
        this.studyField=studyField;

    }
    public Faculty(String name, String abbreviation, StudyField studyField){
        this.name=name;
        this.abbreviation=abbreviation;
        this.students=students;
        this.studyField=studyField;

    }

    public StudyField getStudyField() {
        return studyField;
    }

    public void setStudyField(StudyField studyField) {
        this.studyField = studyField;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void getFacultyInfo(){
        System.out.println(String.format("Name: %s, Abbrev.: %s, Field: %s",name,abbreviation,studyField));
    }

}
