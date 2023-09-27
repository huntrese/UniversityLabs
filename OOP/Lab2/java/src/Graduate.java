package src;

import java.util.Date;
import java.io.*;

class Graduate implements Serializable{

    private String firstName;
    private String lastName;
    private String email;
    private Date enrollmentDate;
    private Date dateOfBirth;
    private static final long serialVersionUID = 1L;


    public Graduate(String firstName,String lastName,String email, Date enrollmentDate, Date dateOfBirth){
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.enrollmentDate=enrollmentDate;
        this.dateOfBirth=dateOfBirth;

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
}