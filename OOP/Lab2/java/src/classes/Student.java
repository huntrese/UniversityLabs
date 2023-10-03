package src.classes;

import java.util.Date;
import java.io.*;

public class Student implements Serializable{

    private String firstName;
    private String lastName;
    private String email;
    private Date enrollmentDate;
    private Date dateOfBirth;
    private String faculty;

    private static final long serialVersionUID = 1L;



    public Student(String firstName,String lastName,String email, Date enrollmentDate, Date dateOfBirth, String faculty){
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.enrollmentDate=enrollmentDate;
        this.dateOfBirth=dateOfBirth;
        this.faculty=faculty;

    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getFaculty() {
        return faculty;
    }

    public void getStudentInfo(){
        System.out.println(String.format("Name: %s, Surname: %s, Email: %s, Faculty: %s, Enrolled: %s, Born: %s",firstName,lastName,email,faculty,enrollmentDate,dateOfBirth));
    }

}