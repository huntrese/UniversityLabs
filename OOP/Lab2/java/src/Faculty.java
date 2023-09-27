package src;

import java.util.List;
import java.io.*;
public class Faculty implements  Serializable {
    private String name;
    private String abbreviation;
    private List<Student> students;
    private StudyField.FIELDS studyField;
    private static final long serialVersionUID = 1L;


    public Faculty(String name, String abbreviation, List<Student> students, StudyField.FIELDS studyField){
        this.name=name;
        this.abbreviation=abbreviation;
        this.students=students;
        this.studyField=studyField;

    }

    public StudyField.FIELDS getStudyField() {
        return studyField;
    }

    public void setStudyField(StudyField.FIELDS studyField) {
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



}
