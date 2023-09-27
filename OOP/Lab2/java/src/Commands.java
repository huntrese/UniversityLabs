package src;

import javax.naming.NamingEnumeration;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Commands {
    public void as(String abbreviation, String firstName, String lastName, String email, Date enrollmentDate, Date dateOfBirth) {
        Student student = new Student(firstName, lastName, email, enrollmentDate, dateOfBirth);
        Reader reader = new Reader();
        Writer writer = new Writer();
        List<Faculty> facultyList = reader.readObjects("src/docs/Faculty.ser", Faculty.class);
        for (Faculty faculty : facultyList) {
            if (faculty.getAbbreviation().equals(abbreviation)) {
                faculty.getStudents().add(student);
                writer.writeObjects(facultyList);
                break;
            }
        }
    }

    public void gs(String abbreviation, String firstName, String lastName, String email) {
        Reader reader = new Reader();
        Writer writer = new Writer();
        List<Faculty> facultyList = reader.readObjects("src/docs/Faculty.ser", Faculty.class);

        for (Faculty faculty : facultyList) {
            if (faculty.getAbbreviation().equals(abbreviation)) {
                List<Student> studentList = faculty.getStudents();
                Iterator<Student> iterator = studentList.iterator();

                while (iterator.hasNext()) {
                    Student student = iterator.next();
                    if (student.getFirstName().equals(firstName) && student.getLastName().equals(lastName) && student.getEmail().equals(email)) {
                        Graduate graduate = new Graduate(student.getFirstName(), student.getLastName(), student.getEmail(), student.getEnrollmentDate(), student.getDateOfBirth());
                        List<Graduate> graduateList = reader.readObjects("src/docs/Graduate.ser", Graduate.class);
                        graduateList.add(graduate);
                        writer.writeObjects(graduateList);
                        iterator.remove(); // Safely remove the student
                    }
                }
                writer.writeObjects(facultyList);
                break;
            }
        }
    }
    public void de() {
        Reader reader = new Reader();
        List<Student> studentList = reader.readObjects("src/docs/Student.ser", Student.class);

        for (Student student : studentList) {
            student.getStudentInfo();
        }
    }
    public void dg() {
        Reader reader = new Reader();
        List<Graduate> graduateList = reader.readObjects("src/docs/Graduate.ser", Graduate.class);

        for (Graduate student : graduateList) {
            student.getStudentInfo();
        }
    }
    public void b(String abbreviation, String firstName, String lastName, String email) {
//        Student student = new Student(firstName, lastName, email, enrollmentDate, dateOfBirth);
        Reader reader = new Reader();
        Boolean found = Boolean.FALSE;
//        Writer writer = new Writer();
        List<Faculty> facultyList = reader.readObjects("src/docs/Faculty.ser", Faculty.class);
        for (Faculty faculty : facultyList) {
            if (faculty.getAbbreviation().equals(abbreviation)) {
                List<Student> studentList = faculty.getStudents();
                for (Student student : studentList){
                    if (student.getFirstName().equals(firstName) && student.getLastName().equals(lastName) && student.getEmail().equals(email)){
                        found = Boolean.TRUE;
                    }
                }
                //                break;
            }
        }
        System.out.println(String.format("%b",found));

    }
    public void cf(String name, String abbreviation, StudyField.FIELDS studyField) {
//        Student student = new Student(firstName, lastName, email, enrollmentDate, dateOfBirth);
        Reader reader = new Reader();
//        Boolean found = Boolean.FALSE;
        Writer writer = new Writer();
        List<Faculty> facultyList = reader.readObjects("src/docs/Faculty.ser", Faculty.class);
        facultyList.add(new Faculty(name,abbreviation,studyField));
//        System.out.println(String.format("%b",found));
        writer.writeObjects(facultyList);
    }

}
